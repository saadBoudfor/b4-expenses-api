package fr.b4.apps.expenses.services;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.common.entities.Address;
import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.AddressRepository;
import fr.b4.apps.common.repositories.PlaceRepository;
import fr.b4.apps.common.repositories.ProductRepository;
import fr.b4.apps.common.services.CategoryService;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.expenses.repositories.ExpenseLineRepository;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static fr.b4.apps.expenses.util.ExpenseUtils.getCurrentTargetDate;


@Component
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseLineRepository expenseLineRepository;
    private final PlaceRepository placeRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ExpenseService(ExpenseRepository expenseRepository,
                          ExpenseLineRepository expenseLineRepository,
                          PlaceRepository placeRepository,
                          AddressRepository addressRepository,
                          ProductRepository productRepository,
                          CategoryService categoryService) {
        this.expenseRepository = expenseRepository;
        this.expenseLineRepository = expenseLineRepository;
        this.placeRepository = placeRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Expense save(Expense expense) {
        if (!ObjectUtils.isEmpty(expense.getPlace()) && ObjectUtils.isEmpty(expense.getPlace().getId())) {
            // create place first
            Address saved = addressRepository.save(expense.getPlace().getAddress());
            expense.getPlace().setAddress(saved);

            Place savedPlace = placeRepository.save(expense.getPlace());
            expense.setPlace(savedPlace);
        }
        expense = expenseRepository.save(expense);
        for (ExpenseLine expenseLine : expense.getExpenseLines()) {
            expenseLine.setExpense(expense);
        }
        List<Product> products = expense
                .getExpenseLines()
                .stream()
                .map(this::checkProduct)
                .filter(product -> !ObjectUtils.isEmpty(product))
                .collect(Collectors.toList());
        products.forEach(product -> categoryService.saveAll(product.getCategories()));
        if (!CollectionUtils.isEmpty(products))
            productRepository.saveAll(products);
        expenseLineRepository.saveAll(expense.getExpenseLines());
        return expenseRepository.save(expense);
    }

    private Product checkProduct(ExpenseLine expenseLine) {
        if (ObjectUtils.isEmpty(expenseLine.getProduct()))
            return null;
        Product found = productRepository.findFirstByName(expenseLine.getProduct().getName());
        if (!ObjectUtils.isEmpty(found))
            expenseLine.getProduct().setId(found.getId());
        return expenseLine.getProduct();
    }

    public Float getTotal(Long userID) {
        Float total = expenseRepository.getAmountExpense(userID, getCurrentTargetDate());
        return ObjectUtils.isEmpty(total) ? 0 : total;
    }

    public int getRestaurantCount(Long userID) {
        return expenseRepository.countExpenses(userID, PlaceType.RESTAURANT.toString());
    }

    public int getStoreCount(Long userID) {
        return expenseRepository.countExpenses(userID, PlaceType.STORE.toString());
    }

    public Float getTotalRestaurant(Long userID) {
        return expenseRepository.sumExpenses(userID, PlaceType.RESTAURANT.toString());
    }

    public Float getTotalStore(Long userID) {
        return expenseRepository.sumExpenses(userID, PlaceType.STORE.toString());
    }

    public List<Expense> findByUser(User user, Integer page, Integer size) {
        if (ObjectUtils.isEmpty(size))
            return expenseRepository.findByUser(user);
        Pageable pageable = Pageable.unpaged();
        if (!ObjectUtils.isEmpty(page) && !ObjectUtils.isEmpty(size)) {
            pageable = PageRequest.of(page, size, Sort.by("date").descending());
        }
        List<Expense> results = expenseRepository.findByUser(user, pageable);
        return CollectionUtils.isEmpty(results) ? new ArrayList<>() : results;
    }

    public int getCurrentWeekTotal(Long userID) {
        LocalDate now = LocalDate.now();
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        LocalDate firstDayOfCurrentWeek = now.with(fieldISO, 1);

        return expenseRepository.getCurrentWeekTotal(userID, firstDayOfCurrentWeek);
    }

    public List<Expense> findByPlaceID(User user, Place place) {
        List<Expense> results = expenseRepository.findByUserAndPlace(user, place);
        return CollectionUtils.isEmpty(results) ? new ArrayList<>() : results;
    }

    public Expense findByID(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(Long expenseID) {
        expenseLineRepository.deleteAllByExpenseId(expenseID);
        expenseRepository.deleteById(expenseID);
    }
}
