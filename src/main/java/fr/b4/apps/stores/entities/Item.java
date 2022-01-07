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
}
