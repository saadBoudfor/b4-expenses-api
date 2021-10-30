package fr.b4.apps.expenses.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.b4.apps.clients.entities.User;
import fr.b4.apps.common.entities.Place;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExpenseDTO {
    private Long id;
    private String name;
    private List<ExpenseLineDTO> expenseLines;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private User author;
    private User user;
    private Place place;
    private String bill;

}
