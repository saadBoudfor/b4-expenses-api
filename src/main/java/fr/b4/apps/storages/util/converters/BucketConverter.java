package fr.b4.apps.storages.util.converters;

import fr.b4.apps.storages.dto.BucketDTO;
import fr.b4.apps.storages.dto.StorageDTO;
import fr.b4.apps.storages.entities.Bucket;
import fr.b4.apps.storages.entities.Storage;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BucketConverter {

    public static List<BucketDTO> toDTO(@Nullable List<Bucket> bucketList) {
        if (CollectionUtils.isEmpty(bucketList)) {
            return new ArrayList<>();
        }
        return bucketList.stream().map(BucketConverter::toDTO).collect(Collectors.toList());
    }

    public static BucketDTO toDTO(Bucket bucket) {
        BucketDTO dto = new BucketDTO();
        dto.setId(bucket.getId());
        dto.setName(bucket.getName());
        dto.setOwner(bucket.getOwner());

        Storage storage = bucket.getStorage();
        if (!ObjectUtils.isEmpty(bucket.getStorage())) {
            StorageDTO storageDTO = new StorageDTO();
            storageDTO.setName(storage.getName());
            storageDTO.setOwner(storage.getOwner());
            storageDTO.setPlanUrl(storage.getPlanUrl());
            storageDTO.setId(storage.getId());
            storageDTO.setDescription(storage.getDescription());
            dto.setStorage(storageDTO);
        }
        return dto;
    }

    public static Bucket toBucket(BucketDTO dto) {
        Bucket bucket = new Bucket();
        bucket.setId(dto.getId());
        bucket.setName(dto.getName());
        bucket.setOwner(dto.getOwner());
        if (!ObjectUtils.isEmpty(dto.getStorage())) {
            Storage storage = new Storage();
            storage.setId(dto.getStorage().getId());
            storage.setOwner(dto.getStorage().getOwner());
            storage.setDescription(dto.getStorage().getDescription());
            storage.setName(dto.getStorage().getName());
            bucket.setStorage(storage);
        }
        return bucket;
    }

    public static List<Bucket> toBucket(List<BucketDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return new ArrayList<>();
        }
        return dtos.stream().map(BucketConverter::toBucket).collect(Collectors.toList());
    }
}
