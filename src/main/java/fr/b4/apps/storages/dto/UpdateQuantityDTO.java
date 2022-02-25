package fr.b4.apps.storages.dto;

import fr.b4.apps.clients.entities.User;
import lombok.Data;

@Data
public class UpdateQuantityDTO {
    private Long id;
    private Float quantity;
    private String comment;
    private User author;
}
