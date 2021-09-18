package fr.b4.apps.expenses.entities;

import fr.b4.apps.common.entities.Product;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class ExpenseLine {
    @Id
    @GeneratedValue
    private Long id;

    @ApiModelProperty(value = "Purchased product", required = true)
    @ManyToOne
    private Product product;

    @ApiModelProperty(value = "Purchased product price in euros", example = "34.3")
    private Float price;

    @ApiModelProperty(value = "Purchased product quantity", example = "5")
    private Float quantity;

    @ApiModelProperty(value = "Client comment")
    private String comment;

    @ManyToOne
    private Expense expense;
}
