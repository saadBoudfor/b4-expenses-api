package fr.b4.apps.expenses.dto;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.expenses.entities.ExpenseLine;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExpenseDTO {
    private Long id;
    private String name;
    private List<ExpenseLineDTO> expenseLines;
    private String comment;
    private LocalDate date;
    private User author;
    private User user;
}
