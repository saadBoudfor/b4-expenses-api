package fr.b4.apps.common.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
}
