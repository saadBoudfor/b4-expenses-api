package fr.b4.apps.storages.dto;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.storages.dto.enums.ConnectedApp;
import fr.b4.apps.storages.entities.Item;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UpdateQuantity {
    @Id @GeneratedValue
    private Long id;
    private Float quantity;
    private String comment;

    @ManyToOne
    private Item item;

    @ManyToOne
    private User author;

    /**
     *  Name of the module that update item (if was updated by application)
     */
    @Enumerated(EnumType.STRING)
    private ConnectedApp module;
}
