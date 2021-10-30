package fr.b4.apps.common.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Unit {
    @Id
    @GeneratedValue
    private Long id;

    private String value;
}
