package fr.b4.apps.common.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private Float quantity;

    private String name;

    private String qrCode;

    private String comment;

    private Integer calories;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String photo;

    @ManyToOne
    private Unit unit;

    private String displayQuantity;

    private String brand;

    private String dataPer;

    @OneToOne
    private NutrientLevels nutrientLevels;

    private String score;

    @ManyToMany
    private List<Category> categories;
}
