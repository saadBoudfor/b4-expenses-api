package fr.b4.apps.storages.services;

import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.storages.dto.BucketDTO;
import fr.b4.apps.storages.entities.Bucket;
import fr.b4.apps.storages.repositories.BucketRepository;
import fr.b4.apps.storages.util.converters.BucketConverter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BucketService {
    private final BucketRepository bucketRepository;

    public BucketService(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    public BucketDTO save(BucketDTO dto) {
        Bucket bucket = BucketConverter.toBucket(dto);
        Bucket saved = bucketRepository.save(bucket);
        return BucketConverter.toDTO(saved);
    }

    public BucketDTO update(@NonNull BucketDTO bucket) {
        Bucket found = bucketRepository.getById(bucket.getId());
        if (ObjectUtils.isEmpty(found)) {
            log.error("Failed to update bucket. Bucket {} unknown", bucket);
            throw new ResourceNotFoundException("Failed to update bucket. Bucket unknown");
        }
        found.setName(bucket.getName());
        found = bucketRepository.save(found);
        return BucketConverter.toDTO(found);
    }

    public void delete(@NonNull long bucketID) {
        if (!bucketRepository.existsById(bucketID)) {
            log.error("Failed to delete Bucket {}. Bucket unknown", bucketID);
            throw new ResourceNotFoundException("Failed to delete Bucket " + bucketID + ". Bucket unknown");
        }
        bucketRepository.deleteById(bucketID);
    }

    public List<BucketDTO> filterByStoreId(@NonNull long id) {
        List<Bucket> found = bucketRepository.findByStoreId(id);
        return CollectionUtils.isEmpty(found) ? new ArrayList<>() : BucketConverter.toDTO(found);
    }

    public List<BucketDTO> filterByUserId(@NonNull long id) {
        List<Bucket> found = bucketRepository.findByOwnerId(id);
        return CollectionUtils.isEmpty(found) ? new ArrayList<>() : BucketConverter.toDTO(found);
    }
}
