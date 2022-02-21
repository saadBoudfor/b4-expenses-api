package fr.b4.apps.storages.dto;

import fr.b4.apps.storages.entities.Item;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class UpdateQuantity {
    @Id @GeneratedValue
    private Long id;
    private Double quantity;
    private String comment;
    @ManyToOne
    private Item item;
}
