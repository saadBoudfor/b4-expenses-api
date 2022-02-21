package fr.b4.apps.storages.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import fr.b4.apps.clients.entities.User;
import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemDTO {
    private Long id;
    private ExpenseDTO expense;

    // required
    private BucketDTO location;
    // required
    private User author;

    private Float quantity;
    private Float remaining;
    private ProductDTO product;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate addDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openDate;

    @JsonProperty("expirationAfter")
    private DurationDTO expirationAfter;
}
