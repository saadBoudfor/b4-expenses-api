package fr.b4.apps.stores.repositories;

import fr.b4.apps.stores.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByAuthorId(Long authorId);

    List<Item> findByLocationId(Long authorId);
}
