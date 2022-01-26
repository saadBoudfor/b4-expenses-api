package fr.b4.apps.storages.util.converters;

import fr.b4.apps.storages.dto.BucketDTO;
import fr.b4.apps.storages.entities.Bucket;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BucketConverter {

    public static List<BucketDTO> toDTO(List<Bucket> bucketList) {
        return bucketList.stream().map(BucketConverter::toDTO).collect(Collectors.toList());
    }
    public static BucketDTO toDTO(Bucket bucket) {
        BucketDTO dto = new BucketDTO();
        dto.setId(bucket.getId());
        dto.setName(bucket.getName());
        dto.setOwner(bucket.getOwner());
        dto.setStore(StoragesConverters.toDTO(bucket.getStorage()));
        return dto;
    }

    public static Bucket toBucket(BucketDTO dto) {
        Bucket bucket = new Bucket();
        bucket.setId(dto.getId());
        bucket.setName(dto.getName());
        bucket.setOwner(dto.getOwner());
        bucket.setStorage(StoragesConverters.toStore(dto.getStore()));
        return bucket;
    }
}
