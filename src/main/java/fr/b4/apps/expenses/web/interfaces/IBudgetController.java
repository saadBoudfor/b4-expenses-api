package fr.b4.apps.expenses.web.interfaces;

import fr.b4.apps.expenses.entities.Budget;
import io.swagger.annotations.*;

import java.util.List;

@Api(value = "Expenses API",  tags = "Budgets", protocols = "http")
public interface IBudgetController {
    @ApiOperation(value = "Define/update user budget", response = Budget.class, tags = "Budgets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Budget updated success", response = Budget.class) })
    public Budget saveBudget(@ApiParam(required = true, example = "15", value = "user id") String accessToken, Budget budget);

    @ApiOperation(value = "Get budget history for given user", response = Budget[].class, tags = "Budgets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Budget updated success", response = Budget[].class) })
    public List<Budget> getAll(@ApiParam(required = true, example = "15", value = "user id") String accessToken);
}
