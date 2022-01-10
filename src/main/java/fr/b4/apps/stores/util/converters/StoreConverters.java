package fr.b4.apps.stores.util.converters;

import fr.b4.apps.stores.dto.StoreDTO;
import fr.b4.apps.stores.entities.Store;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

@UtilityClass
public class StoreConverters {
    public static Store toStore(StoreDTO dto) {
        if(ObjectUtils.isEmpty(dto)) {
            return null;
        }
        Store store = new Store();
        store.setName(dto.getName());
        store.setOwner(dto.getOwner());
        store.setPlanUrl(dto.getPlanUrl());
        store.setId(dto.getId());
        return store;
    }

    public static StoreDTO toDTO(Store store) {
        if(ObjectUtils.isEmpty(store)) {
            return null;
        }
        StoreDTO dto = new StoreDTO();
        dto.setName(store.getName());
        dto.setOwner(store.getOwner());
        dto.setPlanUrl(store.getPlanUrl());
        dto.setId(store.getId());
        return dto;
    }
}
