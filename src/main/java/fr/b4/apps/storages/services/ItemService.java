package fr.b4.apps.storages.services;

import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.storages.dto.ItemDTO;
import fr.b4.apps.storages.entities.Item;
import fr.b4.apps.storages.repositories.ItemRepository;
import fr.b4.apps.storages.util.converters.ItemConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemDTO update(ItemDTO itemDTO) {
        Item item = itemRepository.getById(itemDTO.getId());
        if (ObjectUtils.isEmpty(item)) {
            log.error("Failed to update item: cannot find item with id: " + itemDTO.getId());
            throw new ResourceNotFoundException("cannot find item with id: " + itemDTO.getId());
        }
        item.setQuantity(itemDTO.getQuantity());
        item.setRemaining(itemDTO.getRemaining());
        item = itemRepository.save(item);
        return ItemConverter.toDTO(item);
    }

    public List<ItemDTO> filterByLocationId(Long locationId) {
        List<Item> found = itemRepository.findByLocationId(locationId);
        return CollectionUtils.isEmpty(found) ? new ArrayList<>() : ItemConverter.toDTO(found);
    }

    public List<ItemDTO> filterByAuthorId(Long userId) {
        List<Item> found = itemRepository.findByAuthorId(userId);
        return CollectionUtils.isEmpty(found) ? new ArrayList<>() : ItemConverter.toDTO(found);
    }

    public void delete(Long storeId) {
        if (!itemRepository.existsById(storeId)) {
            log.error("Failed to delete Item {}. Item unknown", storeId);
            throw new ResourceNotFoundException("Failed to delete Item " + storeId + ". Item unknown");
        }
        itemRepository.deleteById(storeId);
    }
}
