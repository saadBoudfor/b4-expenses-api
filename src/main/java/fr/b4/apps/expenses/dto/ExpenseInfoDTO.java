package fr.b4.apps.expenses.dto;

import lombok.Data;

@Data
public class ExpenseInfoDTO {
    private Float total;
    private Float target;
    private Float totalRestaurant;
    private Float totalStore;
    private Float countRestaurant;
    private Float countStore;
    private Integer weekCount;
    private Float weekTotal;

    private Integer weekCountForRestaurant;
    private Float weekTotalForRestaurant;

    private Integer weekCountForStore;
    private Float weekTotalForStore;
}
