package fr.b4.apps.expenses.services;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.expenses.entities.Budget;
import fr.b4.apps.expenses.repositories.BudgetRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

import static fr.b4.apps.expenses.util.ExpenseUtils.getFistDayOfCurrentMonth;

@Component
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget saveCurrentMonthBudget(Float target, User user) {
        LocalDate currentTargetDate = getFistDayOfCurrentMonth();
        List<Budget> found = budgetRepository.getBudgetByDateAndUserId(currentTargetDate, user.getId());
        Budget budget;
        if (CollectionUtils.isEmpty(found)) {
            budget = new Budget();
            budget.setDate(currentTargetDate);
            budget.setTarget(target);
            budget.setUser(user);
        } else {
            budget = found.get(0);
            budget.setTarget(target);
        }
        return budgetRepository.save(budget);

    }

    public Float getTarget(Long userID) {
        LocalDate currentTargetDate = getFistDayOfCurrentMonth();
        List<Budget> found = budgetRepository.getBudgetByDateAndUserId(currentTargetDate, userID);
        return CollectionUtils.isEmpty(found) ? 0.0f : found.get(0).getTarget();
    }

    public List<Budget> getByUserID(Long userID) {
        return budgetRepository.getByUserId(userID);
    }

    public Budget getCurrentByUserID(Long userID) {
        return budgetRepository.getByUserId(userID).get(0);
    }
}
