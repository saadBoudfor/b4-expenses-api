package fr.b4.apps.stores.entities;

import fr.b4.apps.clients.entities.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Store {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String planUrl;

    @ManyToOne
    private User owner;
}
