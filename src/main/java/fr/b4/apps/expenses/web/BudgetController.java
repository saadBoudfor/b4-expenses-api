package fr.b4.apps.expenses.web;

import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.expenses.entities.Budget;
import fr.b4.apps.expenses.services.BudgetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("budget")
@RestController
public class BudgetController {
    private final UserRepository userRepository;
    private final BudgetService budgetService;

    public BudgetController(UserRepository userRepository, BudgetService budgetService) {
        this.userRepository = userRepository;
        this.budgetService = budgetService;
    }

    @PutMapping("/define")
    public Budget saveBudget(@RequestHeader("access-token") String accessToken, @RequestBody Budget budget) {
        userRepository.findById(Long.valueOf(accessToken)).ifPresent(budget::setUser);
        return budgetService.saveCurrentMonthBudget(budget.getTarget(), budget.getUser());
    }

    @GetMapping
    public List<Budget> getAll(String accessToken) {
        return budgetService.getByUserID(Long.valueOf(accessToken));
    }

    @GetMapping("/current")
    public Budget get(@RequestHeader("access-token") String accessToken) {
        return budgetService.getCurrentByUserID(Long.valueOf(accessToken));
    }
}
