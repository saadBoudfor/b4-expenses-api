package fr.b4.apps.common.entities;

import lombok.Data;

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

    @ManyToMany
    private List<ProductCategory> productCategories;

    private String photo;

    private String unit;


    private String brand;

    private String dataPer;

    @OneToOne
    private NutrientLevels nutrientLevels;

    private String score;

    @ManyToMany
    private List<Category> categories;

    // quantity
    private String displayQuantity;
    private String servingQuantity;
    private String servingSize;
    private String productQuantity;
}
