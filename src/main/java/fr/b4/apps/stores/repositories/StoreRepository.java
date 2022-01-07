package fr.b4.apps.stores.repositories;

import fr.b4.apps.stores.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository  extends JpaRepository<Store, Long> {
    List<Store> getByOwnerId(Long id);
}
