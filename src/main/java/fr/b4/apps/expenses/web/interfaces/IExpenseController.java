package fr.b4.apps.expenses.web.interfaces;

import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.entities.Expense;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

@Api(value = "Expenses API",  tags = "Expenses", protocols = "http")
public interface IExpenseController {
    @ApiOperation(value = "Add new expense to user expenses list", response = Expense.class, tags = "Expenses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Expense saved success", response = Expense.class) })
    public ResponseEntity<Object> save(@ApiParam(required = true, example = "sboudfor", value = "username") String accessToken, ExpenseDTO expenseDTO);
}
