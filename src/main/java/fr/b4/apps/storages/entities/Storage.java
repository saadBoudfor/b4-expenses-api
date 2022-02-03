package fr.b4.apps.storages.entities;

import fr.b4.apps.clients.entities.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Storage {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String planUrl;

    private String description;

    @OneToMany(mappedBy = "storage")
    private List<Bucket> buckets;

    @ManyToOne
    private User owner;

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planUrl='" + planUrl + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                '}';
    }
}
