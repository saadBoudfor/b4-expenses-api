package fr.b4.apps.common.dto;

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

    private List<ProductCategoryDTO> productCategories;

    private String photo;

    private String unit;


    private String brand;

    private String dataPer;

    private NutrientLevelsDTO nutrientLevels;

    private String score;

    private List<String> categories;

    // Quantity
    private String displayQuantity;
    private String servingQuantity;
    private String servingSize;
    private String productQuantity;
}
