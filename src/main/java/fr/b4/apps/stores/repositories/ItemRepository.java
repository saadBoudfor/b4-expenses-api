package fr.b4.apps.stores.repositories;

import fr.b4.apps.stores.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
