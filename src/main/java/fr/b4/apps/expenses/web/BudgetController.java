package fr.b4.apps.expenses.web;

import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.expenses.dto.MessageDTO;
import fr.b4.apps.expenses.entities.Budget;
import fr.b4.apps.expenses.services.BudgetService;
import fr.b4.apps.expenses.web.interfaces.IBudgetController;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RequestMapping("budget")
@RestController
public class BudgetController implements IBudgetController {
    private final UserRepository userRepository;
    private final BudgetService budgetService;

    public BudgetController(UserRepository userRepository, BudgetService budgetService) {
        this.userRepository = userRepository;
        this.budgetService = budgetService;
    }

    @PutMapping("/define")
    @Override
    public ResponseEntity<Object> saveBudget(@RequestHeader("access-token") String accessToken, @RequestBody Budget budget) {
        if (StringUtils.hasLength(accessToken)) {
            userRepository.findById(Long.valueOf(accessToken)).ifPresent(budget::setUser);
            if (ObjectUtils.isEmpty(budget.getUser())) {
                return ResponseEntity.status(401).body(new MessageDTO("Unknown user"));
            }
            return ResponseEntity.ok(budgetService.saveCurrentMonthBudget(budget.getTarget(), budget.getUser()));
        }
        return ResponseEntity.status(403).body(new MessageDTO("Token missing in headers"));
    }
}
