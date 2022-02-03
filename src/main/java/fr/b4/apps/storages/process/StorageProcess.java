package fr.b4.apps.storages.process;

import fr.b4.apps.common.exceptions.ResourceNotSavedException;
import fr.b4.apps.storages.dto.StorageDTO;
import fr.b4.apps.storages.entities.Bucket;
import fr.b4.apps.storages.entities.Storage;
import fr.b4.apps.storages.repositories.BucketRepository;
import fr.b4.apps.storages.repositories.StoragesRepository;
import fr.b4.apps.storages.util.converters.BucketConverter;
import fr.b4.apps.storages.util.converters.StoragesConverters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Component
public class StorageProcess {
    private final StoragesRepository storagesRepository;
    private final BucketRepository bucketRepository;

    public StorageProcess(StoragesRepository storagesRepository, BucketRepository bucketRepository) {
        this.storagesRepository = storagesRepository;
        this.bucketRepository = bucketRepository;
    }

    public StorageDTO save(StorageDTO dto) {
        Storage storage = StoragesConverters.toStore(dto);

        // 1. save storage
        if (ObjectUtils.isEmpty(storage)) {
            throw new IllegalArgumentException("storage object cannot be empty");
        }

        storage = storagesRepository.save(storage);
        StorageDTO saved = StoragesConverters.toDTO(storage);
        if (ObjectUtils.isEmpty(saved)) {
            throw new ResourceNotSavedException("failed to save storage: " + dto.getName());
        }

        // 2. update and save buckets
        List<Bucket> buckets = BucketConverter.toBucket(dto.getBuckets());

        for (Bucket bucket : buckets) {
            bucket.setStorage(storage);
        }
        buckets = bucketRepository.saveAll(buckets);

        saved.setBuckets(BucketConverter.toDTO(buckets));
        return saved;
    }
}
