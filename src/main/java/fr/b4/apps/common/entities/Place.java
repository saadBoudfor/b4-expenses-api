package fr.b4.apps.common.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode
@Data
@Entity
public class Place {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private PlaceType type;
}
