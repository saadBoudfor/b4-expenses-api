package fr.b4.apps.common.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ProductCategory {
    @Id
    private String label;

    private String imgURL;

    @ManyToMany(mappedBy = "productCategories")
    List<Product> productList = new ArrayList<>();
}
