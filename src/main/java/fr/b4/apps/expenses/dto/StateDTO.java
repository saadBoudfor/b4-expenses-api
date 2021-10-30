package fr.b4.apps.expenses.dto;

import lombok.Data;

import java.util.List;

@Data
public class StateDTO {
    private List<ExpensePlaceDTO> expensePlaceList;
}
