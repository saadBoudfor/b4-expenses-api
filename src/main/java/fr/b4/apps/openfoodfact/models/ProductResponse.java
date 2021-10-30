package fr.b4.apps.openfoodfact.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {

    private List<OFProduct> products;

    private String code;

    private boolean status;

    @JsonProperty("status_verbose")
    private String statusVerbose;

}
