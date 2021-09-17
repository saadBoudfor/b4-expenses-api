package fr.b4.apps.common.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;
    @ApiModelProperty(value = "Street", example = "178 rue jean batiste", required = true)
    private String street;
    @ApiModelProperty(value = "zip code (postal code)", example = "13005", required = true)
    private String zipCode;

    @ApiModelProperty(value = "City", example = "Marseille", required = true)
    private String City;

    @ApiModelProperty(value = "Country", example = "France", required = true)
    private String Country;
}
