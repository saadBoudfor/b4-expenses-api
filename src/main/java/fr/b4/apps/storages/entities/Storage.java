package fr.b4.apps.storages.entities;

import fr.b4.apps.clients.entities.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Storage {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String planUrl;

    private String description;

    @ManyToOne
    private User owner;
}
