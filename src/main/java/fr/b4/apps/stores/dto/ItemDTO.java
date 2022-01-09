package fr.b4.apps.stores.dto;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import lombok.Data;

@Data
public class ItemDTO {
    private Long id;
    private ExpenseDTO expense;

    // required
    private BucketDTO location;
    // required
    private User author;

    private Float quantity;
    private Float remaining;

    public void setRemaining(Float remaining) {
        if (remaining > quantity) {
            throw new IllegalArgumentException("remaining quantity must be smaller than total quantity");
        }
        this.remaining = remaining;
    }

}