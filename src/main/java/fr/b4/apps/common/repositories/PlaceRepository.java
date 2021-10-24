package fr.b4.apps.common.repositories;

import fr.b4.apps.common.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByNameContains(String search);
}
