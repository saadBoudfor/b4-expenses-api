package fr.b4.apps.expenses.web;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.MessageDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.services.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static fr.b4.apps.expenses.converters.ExpenseConverter.toDTO;
import static fr.b4.apps.expenses.converters.ExpenseConverter.toExpense;

@RequestMapping("expenses")
@RestController
public class ExpenseController {
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
            User authenticated = userRepository.findById(Long.valueOf(accessToken)).get();
            Expense expense = toExpense(expenseDTO);
            expense.setAuthor(authenticated);
            expense = expenseService.save(expense);
            return ResponseEntity.ok(toDTO(expense));
        }
        return ResponseEntity.status(403).body(new MessageDTO("Token missing in headers"));
    }


}
