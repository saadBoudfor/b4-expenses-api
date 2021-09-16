package fr.b4.apps.clients.entities;

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
    private String street;
    private String zipCode;
    private String City;
    private String Country;
}
