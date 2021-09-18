package fr.b4.apps.expenses.util.converters;

import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseLineDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class ExpenseConverter {
    public static Expense toExpense(ExpenseDTO dto) {
        Expense expense = new Expense();
        expense.setName(dto.getName());
        expense.setDate(dto.getDate());
        expense.setExpenseLines(dto.getExpenseLines().stream().map(ExpenseConverter::toExpenseLine).collect(Collectors.toList()));
        expense.setComment(dto.getComment());
        expense.setAuthor(dto.getAuthor());
        return expense;
    }

    public static ExpenseLine toExpenseLine(ExpenseLineDTO dto) {
        ExpenseLine expenseLine = new ExpenseLine();
        expenseLine.setProduct(dto.getProduct());
        expenseLine.setPrice(dto.getPrice());
        expenseLine.setQuantity(dto.getQuantity());
        expenseLine.setComment(dto.getComment());
        return expenseLine;
    }

    public static ExpenseDTO toDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setAuthor(expense.getAuthor());
        dto.setName(expense.getName());
        dto.setId(expense.getId());
        dto.setDate(expense.getDate());
        dto.setComment(expense.getComment());
        dto.setAuthor(expense.getAuthor());
        dto.setExpenseLines(expense.getExpenseLines().stream().map(ExpenseConverter::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public static ExpenseLineDTO toDTO(ExpenseLine expenseLine) {
        ExpenseLineDTO dto = new ExpenseLineDTO();
        dto.setId(expenseLine.getId());
        dto.setProduct(expenseLine.getProduct());
        dto.setPrice(expenseLine.getPrice());
        dto.setQuantity(expenseLine.getQuantity());
        dto.setComment(expenseLine.getComment());
        return dto;
    }
}
