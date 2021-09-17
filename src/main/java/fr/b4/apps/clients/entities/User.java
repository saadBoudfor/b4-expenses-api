package fr.b4.apps.clients.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;
    @ApiModelProperty(value = "User first name", example = "saad", required = true)
    private String name;
    @ApiModelProperty(value = "User last name", example = "boudfor", required = true)
    private String lastname;
    @ApiModelProperty(value = "User email address", example = "saad.boudfor@b4expenses.com", required = true)
    private String email;
    @ApiModelProperty(value = "User username", example = "sboudfor")
    private String username;
}
