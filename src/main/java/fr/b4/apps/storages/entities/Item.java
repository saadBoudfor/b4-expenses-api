package fr.b4.apps.storages.entities;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.expenses.entities.Expense;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Slf4j
@Entity
@Data
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Expense expense;

    @ManyToOne
    private Bucket location;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User author;

    private Float quantity;
    private Float remaining;

    private LocalDate expirationDate;
    private LocalDate addDate;
    private LocalDate openDate;

    private Integer expirationAfterDays;
    private Integer expirationAfterHours;
    private Integer expirationAfterMinutes;

    public void setRemaining(Float remaining) {
        String errorMessage = "";
        if (remaining < 0) {
            errorMessage = "Remaining quantity cannot be a negative value";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if (ObjectUtils.isNotEmpty(product) && ObjectUtils.isNotEmpty(product.getProductQuantity())) {
            final float total = quantity * Float.parseFloat(product.getProductQuantity());
            if (total < remaining) {
                errorMessage = "Remaining quantity " + remaining + " cannot exceed total quantity (= " + total + ")";
                log.error(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
        }
        this.remaining = remaining;
    }

    public void setQuantity(Float quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Bought quantity cannot be a negative value");
        }
        this.quantity = quantity;
    }

    private boolean open = false;

}
