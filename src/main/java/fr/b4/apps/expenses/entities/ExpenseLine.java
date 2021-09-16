package fr.b4.apps.expenses.entities;

import fr.b4.apps.common.entities.Product;
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

    @ManyToOne
    private Product product;
    private Float price;
    private Float quantity;
    private String comment;
}
