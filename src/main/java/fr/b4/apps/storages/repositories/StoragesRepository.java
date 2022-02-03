package fr.b4.apps.storages.repositories;

import fr.b4.apps.storages.entities.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoragesRepository extends JpaRepository<Storage, Long> {
    List<Storage> getByOwnerId(Long id);

    boolean existsByName(String name);
}
