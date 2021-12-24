package fr.b4.apps.expenses.services;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.expenses.entities.Budget;
import fr.b4.apps.expenses.repositories.BudgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

import static fr.b4.apps.expenses.util.ExpenseUtils.getFistDayOfCurrentMonth;

@Slf4j
@Component
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    /**
     * Define new target for given user
     *
     * @param target new target
     * @param user   given user
     * @return updated budget
     */
    public Budget defineCurrentMonthBudget(Float target, User user) {
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

    /**
     * Get given user's target for current month
     *
     * @param userID given user id (valid)
     * @return target for current month
     */
    public Float getTarget(Long userID) {
        if (ObjectUtils.isEmpty(userID) || userID <= 0) {
            log.error("User id cannot be null or empty");
            throw new IllegalArgumentException("User id cannot be null or empty");
        }
        LocalDate currentTargetDate = getFistDayOfCurrentMonth();
        List<Budget> found = budgetRepository.getBudgetByDateAndUserId(currentTargetDate, userID);
        return CollectionUtils.isEmpty(found) ? 0.0f : found.get(0).getTarget();
    }

    /**
     * Get budget list for given user
     * budget list is sorted by date (desc)
     *
     * @param userID given user
     * @return budget list
     */
    public List<Budget> getByUserID(Long userID) {
        if (ObjectUtils.isEmpty(userID) || userID <= 0) {
            log.error("User id cannot be null or empty");
            throw new IllegalArgumentException("User id cannot be null or empty");
        }
        return budgetRepository.getByUserId(userID);
    }

    /**
     * Get current month budget for given user
     *
     * @param userID given user
     * @return defined budget for current month
     */
    public Budget getCurrentByUserID(Long userID) {
        List<Budget> allBudget = getByUserID(userID);
        if (CollectionUtils.isEmpty(allBudget))
            throw new ResourceNotFoundException("budget not defined for current");
        return allBudget.get(0);
    }


}
