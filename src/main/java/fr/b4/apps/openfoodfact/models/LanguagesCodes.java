package fr.b4.apps.openfoodfact.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LanguagesCodes {
    @JsonProperty("en")
    private String en;

    @JsonProperty("fr")
    private String fr;
}
