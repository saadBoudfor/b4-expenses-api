package fr.b4.apps.stores.util.converters;

import fr.b4.apps.stores.dto.StoreDTO;
import fr.b4.apps.stores.entities.Store;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StoreConverters {
    public static Store toStore(StoreDTO dto) {
        Store store = new Store();
        store.setName(dto.getName());
        store.setOwner(dto.getOwner());
        store.setPlanUrl(dto.getPlanUrl());
        store.setId(dto.getId());
        return store;
    }

    public static StoreDTO toDTO(Store store) {
        StoreDTO dto = new StoreDTO();
        dto.setName(store.getName());
        dto.setOwner(store.getOwner());
        dto.setPlanUrl(store.getPlanUrl());
        dto.setId(store.getId());
        return dto;
    }
}
