package fr.b4.apps.expenses.web.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseInfoDTO;
import fr.b4.apps.expenses.entities.Expense;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(value = "Expenses API", tags = "Expenses", protocols = "http")
public interface IExpenseController {
    @ApiOperation(value = "Add new expense to user expenses list", response = Expense.class, tags = "Expenses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Expense saved success", response = Expense.class)})
    public ExpenseDTO save(MultipartFile file, String expenseStr) throws IOException;

    public ExpenseInfoDTO get(@ApiParam(required = true, example = "15", value = "user id") String accessToken);

    @ApiOperation(value = "Get expenses for given user", response = Expense.class, tags = "Expenses")
    List<ExpenseDTO> getAll(@RequestHeader("access-token") String accessToken,
                            @RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "size", required = false) Integer size);
}
