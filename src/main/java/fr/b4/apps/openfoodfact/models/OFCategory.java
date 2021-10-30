package fr.b4.apps.openfoodfact.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OFCategory { ;
    @JsonProperty("parents")
    private List<String> parents;

    @JsonProperty("children")
    private List<String> children;

    @JsonProperty("name")
    private LanguagesCodes name;
}
