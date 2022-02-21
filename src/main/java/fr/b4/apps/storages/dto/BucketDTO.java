package fr.b4.apps.storages.dto;

import fr.b4.apps.clients.entities.User;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class BucketDTO {
    private Long id;
    private StorageDTO storage;
    private String name;
    private User owner;
}
