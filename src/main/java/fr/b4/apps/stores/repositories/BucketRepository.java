package fr.b4.apps.stores.repositories;

import fr.b4.apps.stores.entities.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    List<Bucket> findByStoreId(Long id);
    List<Bucket> findByOwnerId(Long id);
}
