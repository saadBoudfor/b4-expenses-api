package fr.b4.apps.expenses.entities;

import fr.b4.apps.clients.entities.User;
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

    private String name;

    @OneToMany
    private List<ExpenseLine> expenseLines;

    private LocalDate date;

    private String comment;

    @ManyToOne
    private User author;
}
