package fr.b4.apps.common.dto;

import fr.b4.apps.common.entities.Category;
import fr.b4.apps.common.entities.NutrientLevels;
import fr.b4.apps.common.entities.ProductCategory;
import fr.b4.apps.common.entities.Unit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
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

    private List<Category> categories;
}
