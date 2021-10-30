package fr.b4.apps.common.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Place {
    @Id
    @GeneratedValue
    private Long id;

    @ApiModelProperty(value = "Place name", example = "Mcdonald", required = true)
    private String name;

    @ApiModelProperty(value = "Place address", required = true)
    @ManyToOne
    private Address address;

    @ApiModelProperty(value = "Place type", example = "STORE3", required = true)
    @Enumerated(EnumType.STRING)
    private PlaceType type;
}
