package fr.b4.apps.storages.dto;

import fr.b4.apps.clients.entities.User;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class StorageDTO {
    private Long id;
    private String name;
    private String planUrl;
    private User owner;
    private String description;
}
