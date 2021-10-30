package fr.b4.apps.common.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Category {

    @Id
    private String id;

    private String fr;

    private String en;

    @ManyToMany
    private List<Category> parents;

    @ManyToMany
    private List<Category> children;
}
