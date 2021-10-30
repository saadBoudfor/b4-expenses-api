package fr.b4.apps.common.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @ApiModelProperty(value = "Quantity of product by unit purchased (example for coca cola: 33cl)", example = "33")
    private Float quantity;

    @ApiModelProperty(value = "Product name", example = "Coca cola zero")
    private String name;

    @ApiModelProperty(value = "Product Bar code", example = "7622210449283")
    private String qrCode;

    @ApiModelProperty(value = "Client comment", example = "Contains chemical ingredient")
    private String comment;

    @ApiModelProperty(value = "Amount of calories by 100g or 100ml", example = "396")
    private Integer calories;

    @ApiModelProperty(value = "Product catergory. Value must be in defined range bellow", example = "DRINK")
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String photo;

    @ManyToOne
    private Unit unit;

    private String displayQuantity;

    private String brand;

    private String dataPer;


    @ManyToMany
    private List<Category> categories;
}
