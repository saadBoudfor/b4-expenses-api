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

import java.util.List;

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
    public ExpenseDTO save(@RequestHeader("access-token") String accessToken,
                                       @RequestBody ExpenseDTO expenseDTO) {
        return toDTO(expenseProcess.save(toExpense(expenseDTO), accessToken));
    }

    @GetMapping("/info")
    @Override
    public ExpenseInfoDTO get(@RequestHeader("access-token") String accessToken) {
        return expenseProcess.getInfo(accessToken);
    }

    @GetMapping
    public List<ExpenseDTO> getAll(@RequestHeader("access-token") String accessToken,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "size", required = false) Integer size) {
        return expenseProcess.findByUserID(accessToken, page, size);
    }

}
