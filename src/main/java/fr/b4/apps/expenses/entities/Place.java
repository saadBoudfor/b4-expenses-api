package fr.b4.apps.expenses.entities;

import fr.b4.apps.clients.entities.Address;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Place {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private PlaceType type;
}
