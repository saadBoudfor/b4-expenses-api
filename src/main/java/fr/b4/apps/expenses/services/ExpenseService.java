package fr.b4.apps.expenses.services;

import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import fr.b4.apps.expenses.repositories.ExpenseLineRepository;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import org.springframework.stereotype.Component;

import static fr.b4.apps.expenses.util.ExpenseUtils.getCurrentTargetDate;


@Component
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseLineRepository expenseLineRepository;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseLineRepository expenseLineRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseLineRepository = expenseLineRepository;
    }

    public Expense save(Expense expense) {
        expense = expenseRepository.save(expense);
        for(ExpenseLine expenseLine: expense.getExpenseLines()) {
            expenseLine.setExpense(expense);
        }
        expenseLineRepository.saveAll(expense.getExpenseLines());
        return expenseRepository.save(expense);
    }

    public Float getTotal(Long userID) {
        return expenseRepository.getAmountExpense(userID, getCurrentTargetDate());
    }
}
