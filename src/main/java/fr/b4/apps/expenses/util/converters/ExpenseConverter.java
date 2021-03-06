package fr.b4.apps.expenses.util.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.util.converters.ProductConverter;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.ExpenseLineDTO;
import fr.b4.apps.expenses.dto.ExpensePlaceDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ExpenseConverter {
    public static Expense toExpense(ExpenseDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return null;
        } else {
            // convert to expense:
            Expense expense = new Expense();
            expense.setId(dto.getId());
            expense.setName(dto.getName());
            expense.setDate(dto.getDate());
            if (!CollectionUtils.isEmpty(dto.getExpenseLines())) {
                expense.setExpenseLines(dto.getExpenseLines()
                        .stream().map(ExpenseConverter::toExpenseLine).collect(Collectors.toList()));
            }
            expense.setComment(dto.getComment());
            expense.setAuthor(dto.getAuthor());
            expense.setPlace(dto.getPlace());
            expense.setUser(dto.getUser());
            expense.setBill(dto.getBill());
            return expense;
        }

    }

    public static ExpenseLine toExpenseLine(ExpenseLineDTO dto) {
        ExpenseLine expenseLine = new ExpenseLine();
        expenseLine.setProduct(ProductConverter.toProduct(dto.getProduct()));
        expenseLine.setPrice(dto.getPrice());
        expenseLine.setQuantity(dto.getQuantity());
        expenseLine.setComment(dto.getComment());
        return expenseLine;
    }

    public static ExpenseDTO toDTO(Expense expense) {
        if (ObjectUtils.isEmpty(expense)) {
            return null;
        } else {
            // convert to dto
            ExpenseDTO dto = new ExpenseDTO();
            dto.setAuthor(expense.getAuthor());
            dto.setName(expense.getName());
            dto.setId(expense.getId());
            dto.setDate(expense.getDate());
            dto.setComment(expense.getComment());
            dto.setAuthor(expense.getAuthor());
            dto.setPlace(expense.getPlace());
            dto.setUser(expense.getUser());
            dto.setBill(expense.getBill());
            if (!CollectionUtils.isEmpty(expense.getExpenseLines()))
                dto.setExpenseLines(expense.getExpenseLines().stream().map(ExpenseConverter::toDTO)
                        .collect(Collectors.toList()));
            return dto;
        }
    }

    public static ExpenseLineDTO toDTO(ExpenseLine expenseLine) {
        ExpenseLineDTO dto = new ExpenseLineDTO();
        dto.setId(expenseLine.getId());
        dto.setProduct(ProductConverter.toDto(expenseLine.getProduct()));
        dto.setPrice(expenseLine.getPrice());
        dto.setQuantity(expenseLine.getQuantity());
        dto.setComment(expenseLine.getComment());
        return dto;
    }

    public static List<ExpenseDTO> toDTO(List<Expense> expenses) {
        return expenses.stream().map(ExpenseConverter::toDTO).collect(Collectors.toList());
    }

    public static ExpenseDTO valueOf(String expenseStr) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        return mapper.readValue(expenseStr, ExpenseDTO.class);
    }

    public static ExpensePlaceDTO convertToExpensePlaceRanking(Object[] item) {
        ExpensePlaceDTO expensePlaceDTO = new ExpensePlaceDTO();
        Place place = new Place();
        place.setId(((BigInteger) item[0]).longValue());
        expensePlaceDTO.setPlace(place);
        expensePlaceDTO.setTotal(((Double) item[1]).longValue());
        expensePlaceDTO.setCount(((BigInteger) item[2]).longValue());
        return expensePlaceDTO;
    }
}
