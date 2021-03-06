package fr.b4.apps.openfoodfact.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OFNutrientLevels {

    private String fat;

    private String salt;

    @JsonProperty("saturated-fat")
    private String saturatedFat;

    private String sugars;
}
