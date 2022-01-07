package fr.b4.apps.stores.entities;

import fr.b4.apps.clients.entities.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Bucket {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Store store;

    private String name;

    @ManyToOne
    private User owner;
}
