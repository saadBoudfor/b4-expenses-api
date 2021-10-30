package fr.b4.apps.expenses.dto;

import lombok.Data;

@Data
public class ExpenseInfoDTO {
    private Float total;
    private Float target;
    private Float totalRestaurant;
    private Float totalStore;
    private int countRestaurant;
    private int countStore;
    private int weekCount;
}
