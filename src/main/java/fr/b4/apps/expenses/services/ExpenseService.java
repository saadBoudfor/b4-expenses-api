package fr.b4.apps.expenses.services;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.common.entities.Address;
import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.repositories.AddressRepository;
import fr.b4.apps.common.repositories.PlaceRepository;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.expenses.repositories.ExpenseLineRepository;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static fr.b4.apps.expenses.util.ExpenseUtils.getCurrentTargetDate;


@Component
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseLineRepository expenseLineRepository;
    private final PlaceRepository placeRepository;
    private final AddressRepository addressRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          ExpenseLineRepository expenseLineRepository,
                          PlaceRepository placeRepository,
                          AddressRepository addressRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseLineRepository = expenseLineRepository;
        this.placeRepository = placeRepository;
        this.addressRepository = addressRepository;
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
        expenseLineRepository.saveAll(expense.getExpenseLines());
        return expenseRepository.save(expense);
    }

    public Float getTotal(Long userID) {
        return expenseRepository.getAmountExpense(userID, getCurrentTargetDate());
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
        Pageable pageable = Pageable.unpaged();
        if (!ObjectUtils.isEmpty(page) && !ObjectUtils.isEmpty(size)) {
            pageable = PageRequest.of(page, size, Sort.by("date").descending());
        }
        List<Expense> results = expenseRepository.findByUser(user, pageable);
        return CollectionUtils.isEmpty(results) ? new ArrayList<>() : results;
    }
}
