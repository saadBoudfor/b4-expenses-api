package fr.b4.apps.storages.util.converters;

import fr.b4.apps.storages.dto.StorageDTO;
import fr.b4.apps.storages.entities.Storage;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

@UtilityClass
public class StoragesConverters {
    public static Storage toStore(StorageDTO dto) {
        if(ObjectUtils.isEmpty(dto)) {
            return null;
        }
        Storage storage = new Storage();
        storage.setName(dto.getName());
        storage.setOwner(dto.getOwner());
        storage.setPlanUrl(dto.getPlanUrl());
        storage.setId(dto.getId());
        storage.setDescription(dto.getDescription());
        return storage;
    }

    public static StorageDTO toDTO(Storage storage) {
        if(ObjectUtils.isEmpty(storage)) {
            return null;
        }
        StorageDTO dto = new StorageDTO();
        dto.setName(storage.getName());
        dto.setOwner(storage.getOwner());
        dto.setPlanUrl(storage.getPlanUrl());
        dto.setId(storage.getId());
        dto.setDescription(storage.getDescription());
        return dto;
    }
}
