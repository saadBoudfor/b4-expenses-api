package fr.b4.apps.storages.repositories;

import fr.b4.apps.storages.entities.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    List<Bucket> findByStorageId(Long id);
    List<Bucket> findByOwnerId(Long id);
}
