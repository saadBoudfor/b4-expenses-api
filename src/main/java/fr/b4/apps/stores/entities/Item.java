package fr.b4.apps.stores.entities;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.expenses.entities.Expense;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Expense expense;

    @ManyToOne
    private Bucket location;

    @ManyToOne
    private User author;

    private Float quantity;
    private Float remaining;

    public void setRemaining(Float remaining) {
        if (remaining > quantity || remaining < 0) {
            throw new IllegalArgumentException("remaining quantity must be smaller than total quantity");
        }
        this.remaining = remaining;
    }

    public void setQuantity(Float quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be a negative value");
        }
        this.quantity = quantity;
    }

}
