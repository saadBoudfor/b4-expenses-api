package fr.b4.apps.common.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class NutrientLevels {

    @Id @GeneratedValue
    private Long id;

    private String fat;

    private String salt;

    private String saturatedFat;

    private String sugars;
}
