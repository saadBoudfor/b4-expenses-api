package fr.b4.apps.storages.dto;

import fr.b4.apps.clients.entities.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateQuantityDTO {
    private Long id;
    private Float quantity;
    private String comment;
    private User author;
    private LocalDateTime updateTime;
}
