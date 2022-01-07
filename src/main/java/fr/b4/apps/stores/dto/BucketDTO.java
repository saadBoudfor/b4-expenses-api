package fr.b4.apps.stores.dto;

import fr.b4.apps.clients.entities.User;
import lombok.Data;

@Data
public class BucketDTO {
    private Long id;
    private StoreDTO store;
    private String name;
    private User owner;
}
