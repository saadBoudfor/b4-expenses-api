package fr.b4.apps.common.dto;

import fr.b4.apps.common.entities.ProductCategory;
import fr.b4.apps.common.entities.Unit;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Long id;

    private Float quantity;

    private String name;

    private String qrCode;

    private String comment;

    private Integer calories;

    private ProductCategory category;

    private String photo;

    private Unit unit;

    private String displayQuantity;

    private String brand;

    private String dataPer;

    private NutrientLevelsDTO nutrientLevels;

    private String score;

    private List<String> categories;
}
