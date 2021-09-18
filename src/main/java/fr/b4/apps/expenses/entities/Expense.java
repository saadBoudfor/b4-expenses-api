package fr.b4.apps.expenses.entities;

import fr.b4.apps.clients.entities.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Expense {
    @Id
    @GeneratedValue
    private Long id;

    @ApiModelProperty(value = "Expense name", example = "Dépense scolaire", required = true)
    private String name;

    @ApiModelProperty(value = "Purchased product", required = true)
    @OneToMany(mappedBy = "expense")
    private List<ExpenseLine> expenseLines;

    @ApiModelProperty(value = "Purchase date", example = "12/09/2021")
    private LocalDate date;

    @ApiModelProperty(value = "User comment", example = "return article 5")
    private String comment;

    @ApiModelProperty(value = "The person who bought the product")
    @ManyToOne
    private User author;

    @ApiModelProperty(value = "Authenticated user account", required = true)
    @ManyToOne
    private User user;

    @ApiModelProperty(value = "Store/Restaurant", required = true)
    @ManyToOne
    private Place place;

}
