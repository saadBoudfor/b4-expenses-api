package fr.b4.apps.expenses.dto;

import fr.b4.apps.common.entities.Place;
import lombok.Data;

@Data
public class ExpensePlaceDTO {
    private Place place;
    private Long total;
    private Long count;
}
