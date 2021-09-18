package fr.b4.apps.expenses.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.b4.apps.clients.entities.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data
public class Budget {
    @Id
    @GeneratedValue
    private Long id;

    @ApiModelProperty(value = "Current Month", required = true, example = "2021-09-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ApiModelProperty(value = "Target value", required = true, example = "300")
    private Float target;

    @ApiModelProperty(value = "Authenticated user")
    @ManyToOne
    private User user;
}
