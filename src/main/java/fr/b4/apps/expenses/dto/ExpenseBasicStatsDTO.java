package fr.b4.apps.expenses.dto;

import lombok.Data;

@Data
public class ExpenseBasicStatsDTO {
    private Float target;

    private Float total;
    private Integer count;

    private Integer countForCurrentWeek;
    private Float totalForCurrentWeek;

}

