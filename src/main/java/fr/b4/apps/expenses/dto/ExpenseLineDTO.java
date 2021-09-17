package fr.b4.apps.expenses.dto;

import fr.b4.apps.common.entities.Product;
import lombok.Data;

@Data
public class ExpenseLineDTO {

    private Long id;
    private Product product;
    private Float price;
    private Float quantity;
    private String comment;
}