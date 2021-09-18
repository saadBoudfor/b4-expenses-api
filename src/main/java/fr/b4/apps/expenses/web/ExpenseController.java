package fr.b4.apps.expenses.web;

import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseInfoDTO;
import fr.b4.apps.expenses.dto.MessageDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.process.ExpenseProcess;
import fr.b4.apps.expenses.web.interfaces.IExpenseController;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import static fr.b4.apps.expenses.util.converters.ExpenseConverter.toDTO;
import static fr.b4.apps.expenses.util.converters.ExpenseConverter.toExpense;

@RequestMapping("expenses")
@RestController
public class ExpenseController implements IExpenseController {

    private final ExpenseProcess expenseProcess;

    public ExpenseController(ExpenseProcess expenseProcess) {
        this.expenseProcess = expenseProcess;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("access-token") String accessToken,
                                       @RequestBody ExpenseDTO expenseDTO) {
        Expense saved = expenseProcess.save(toExpense(expenseDTO), accessToken);
        return ObjectUtils.isEmpty(saved) ?
                ResponseEntity.status(403).body(new MessageDTO("Token missing in headers"))
                : ResponseEntity.ok(toDTO(saved));
    }

    @GetMapping("/info")
    @Override
    public ResponseEntity<Object> get(@RequestHeader("access-token") String accessToken) {
        ExpenseInfoDTO expenseInfoDTO = expenseProcess.getInfo(accessToken);
        return ObjectUtils.isEmpty(expenseInfoDTO) ?
                ResponseEntity.status(403).body(new MessageDTO("Token missing in headers"))
                : ResponseEntity.ok(expenseInfoDTO);
    }

}
