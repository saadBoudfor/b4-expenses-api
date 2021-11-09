package fr.b4.apps.expenses.dto;

import lombok.Data;

import java.util.List;

@Data
public class NutrientStatRecapDTO {
    List<NutrientStatDTO> stats;
}
