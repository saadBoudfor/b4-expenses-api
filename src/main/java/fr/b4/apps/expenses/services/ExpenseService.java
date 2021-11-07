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
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        for (ExpenseLine expenseLine : expense.getExpenseLines()) {
            expenseLine.setExpense(expense);
        }

        expense.getExpenseLines()
                .stream()
                .filter(this::isProductValid)
                .forEach(this::checkProduct);

        expense = expenseRepository.save(expense);
        expenseLineRepository.saveAll(expense.getExpenseLines());
        return expenseRepository.save(expense);
    }

    private boolean isProductValid(ExpenseLine expenseLine) {
        return !ObjectUtils.isEmpty(expenseLine) && !ObjectUtils.isEmpty(expenseLine.getProduct());
    }

    private void checkProduct(ExpenseLine expenseLine) {
        Product found = productRepository.findFirstByName(expenseLine.getProduct().getName());
        if (!ObjectUtils.isEmpty(found)) {
            // to prevent update product stored in db
            expenseLine.setProduct(found);
        } else {
            Product saved = productRepository.save(expenseLine.getProduct());
            // TODO: Manage categories
//            if (!CollectionUtils.isEmpty(saved.getCategories())) {
//                categoryService.saveAll(saved.getCategories());
//            }
            expenseLine.setProduct(saved);
        }
    }

    public Float getTotal(Long userID) {
        Float total = expenseRepository.getAmountExpense(userID, getCurrentTargetDate());
        return ObjectUtils.isEmpty(total) ? 0 : total;
    }

    public Float getRestaurantCount(Long userID) {
        return expenseRepository.countExpenses(userID, PlaceType.RESTAURANT.toString());
    }

    public Float getStoreCount(Long userID) {
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
            return expenseRepository.findByUserOrderByDateDesc(user);
        Pageable pageable = Pageable.unpaged();
        if (!ObjectUtils.isEmpty(page) && !ObjectUtils.isEmpty(size)) {
            pageable = PageRequest.of(page, size);
        }
        List<Expense> results = expenseRepository.findByUserOrderByDateDesc(user, pageable);
        return CollectionUtils.isEmpty(results) ? new ArrayList<>() : results;
    }


    public List<Expense> findTop5ByUser(User user) {
        return expenseRepository.findTop5ByUserOrderByDateDesc(user);
    }

    public Float getCurrentWeekTotal(Long userID) {
        LocalDate now = LocalDate.now();
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        LocalDate firstDayOfCurrentWeek = now.with(fieldISO, 1);

        return expenseRepository.getCurrentWeekTotal(userID, firstDayOfCurrentWeek);
    }


    public Float getCurrentWeekTotalByPlaceType(Long userID, PlaceType type) {
        LocalDate now = LocalDate.now();
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        LocalDate firstDayOfCurrentWeek = now.with(fieldISO, 1);

        return expenseRepository.getCurrentWeekTotalByPlaceType(userID, firstDayOfCurrentWeek, type.toString());
    }


    public Integer getCurrentWeekCountByPlaceType(Long userID, PlaceType type) {
        LocalDate now = LocalDate.now();
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        LocalDate firstDayOfCurrentWeek = now.with(fieldISO, 1);

        return expenseRepository.getCurrentWeekCountByPlaceType(userID, firstDayOfCurrentWeek, type.toString());
    }


    public Integer getCurrentWeekCount(Long userID) {
        LocalDate now = LocalDate.now();
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        LocalDate firstDayOfCurrentWeek = now.with(fieldISO, 1);

        return expenseRepository.getCurrentWeekCount(userID, firstDayOfCurrentWeek);
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

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }
}
