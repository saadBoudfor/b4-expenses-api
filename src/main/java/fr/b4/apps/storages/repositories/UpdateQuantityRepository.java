package fr.b4.apps.storages.repositories;

import fr.b4.apps.storages.dto.UpdateQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateQuantityRepository extends JpaRepository<UpdateQuantity, Long> {
}
