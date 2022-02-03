package fr.b4.apps.expenses.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class MessageDTO {
    private String message;
    private boolean existByName;

    public MessageDTO(String message) {
        this.message = message;
    }

    public MessageDTO(boolean existByName) {
        this.existByName = existByName;
    }
}
