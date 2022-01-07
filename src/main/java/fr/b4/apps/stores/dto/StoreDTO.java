package fr.b4.apps.stores.dto;

import fr.b4.apps.clients.entities.User;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class StoreDTO {
    private Long id;
    private String name;
    private String planUrl;
    private User owner;
}
