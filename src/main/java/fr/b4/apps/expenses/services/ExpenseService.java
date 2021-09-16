package fr.b4.apps.expenses.services;

import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import fr.b4.apps.expenses.repositories.ExpenseLineRepository;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseLineRepository expenseLineRepository;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseLineRepository expenseLineRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseLineRepository = expenseLineRepository;
    }

    public Expense save(Expense expense) {

        List<ExpenseLine> saved = expenseLineRepository.saveAll(expense.getExpenseLines());
        expense.setExpenseLines(saved);
        return expenseRepository.save(expense);
    }
}
