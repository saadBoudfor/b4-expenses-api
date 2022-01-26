package fr.b4.apps.storages.repositories;

import fr.b4.apps.storages.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByAuthorId(Long authorId);

    List<Item> findByLocationId(Long authorId);
}
