package fr.b4.apps.storages.entities;

import fr.b4.apps.clients.entities.User;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Bucket {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Storage storage;

    private String name;

    @ManyToOne
    private User owner;

    @Override
    public String toString() {
        return "Bucket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }
}
