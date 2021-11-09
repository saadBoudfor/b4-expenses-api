package fr.b4.apps.expenses.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private User author;
    private User user;
    private Place place;
    private String bill;

}
