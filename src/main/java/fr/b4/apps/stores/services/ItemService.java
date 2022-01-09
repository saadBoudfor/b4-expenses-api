package fr.b4.apps.stores.services;

import fr.b4.apps.stores.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemService {
    public ItemDTO update(ItemDTO itemDTO) {
        return null;
    }

    public List<ItemDTO> filterByBucketId(Long bucketId) {
        return null;
    }

    public List<ItemDTO> filterByUserId(Long userId) {
        return null;
    }

    public void delete(Long storeId) {

    }
}
