package fr.b4.apps.expenses.web;

import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.MessageDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.web.interfaces.IExpenseController;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static fr.b4.apps.expenses.converters.ExpenseConverter.toDTO;
import static fr.b4.apps.expenses.converters.ExpenseConverter.toExpense;

@RequestMapping("expenses")
@RestController
public class ExpenseController implements IExpenseController {
    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseService expenseService, UserRepository userRepository) {
        this.expenseService = expenseService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("access-token") String accessToken,
                                       @RequestBody ExpenseDTO expenseDTO) {
        if (StringUtils.hasLength(accessToken)) {
            final Expense expense = toExpense(expenseDTO);
            userRepository.findById(Long.valueOf(accessToken)).ifPresent(expense::setUser);
            if (ObjectUtils.isEmpty(expense.getUser())) {
                return ResponseEntity.status(401).body(new MessageDTO("Unknown user"));
            }
            return ResponseEntity.ok(toDTO(expenseService.save(expense)));
        }
        return ResponseEntity.status(403).body(new MessageDTO("Token missing in headers"));
    }


}
