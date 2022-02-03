package fr.b4.apps.storages.services;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;

import fr.b4.apps.storages.dto.ItemDTO;
import fr.b4.apps.storages.entities.Item;
import fr.b4.apps.storages.entities.Storage;
import fr.b4.apps.storages.repositories.ItemRepository;
import fr.b4.apps.storages.util.converters.ItemConverter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTests {

    @Mock
    ItemRepository itemRepository;

    @Test
    public void shouldUpdateSuccess() {
        // Given
        ItemDTO dto = DataGenerator.generateItemDTO(true);
        when(itemRepository.getById(dto.getId())).thenReturn(ItemConverter.toItem(dto));
        when(itemRepository.save(any())).then((Answer<Item>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (Item) args[0];
        });

        // When
        ItemService service = new ItemService(itemRepository);
        ItemDTO updated = service.update(dto);

        // Then
//        dto.getLocation().setStore(null); // expected
        Assertions.assertEquals(dto, updated);
        verify(itemRepository, times(1)).getById(anyLong());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionIfItemUnknown() {
        // Given
        ItemDTO dto = DataGenerator.generateItemDTO(true);
        when(itemRepository.getById(dto.getId())).thenReturn(null);

        // When
        ItemService service = new ItemService(itemRepository);

        // Then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(dto));
        verify(itemRepository, times(1)).getById(anyLong());
        verify(itemRepository, never()).save(any());
    }

    @Test
    public void shouldFilterBucketsByAuthorIdSuccess() {
        // Given
        List<Item> items = DataGenerator.generateItem(5);
        when(itemRepository.findByAuthorId(5L)).thenReturn(items);

        // When
        ItemService service = new ItemService(itemRepository);
        List<ItemDTO> found = service.filterByAuthorId(5L);

        // Then
        Assertions.assertEquals(found, ItemConverter.toDTO(items));
    }


    @Test
    public void shouldReturnEmptyArrayIfNoResultOnFilterByAuthorId() {
        // When
        ItemService service = new ItemService(itemRepository);
        List<ItemDTO> found = service.filterByAuthorId(5L);

        // Then
        Assertions.assertEquals(found, new ArrayList<>());
    }

    @Test
    public void shouldFilterBucketsByLocationIdSuccess() {
        // Given
        List<Item> items = DataGenerator.generateItem(5);
        when(itemRepository.findByLocationId(5L)).thenReturn(items);

        // When
        ItemService service = new ItemService(itemRepository);
        List<ItemDTO> found = service.filterByLocationId(5L);

        // Then
        Assertions.assertEquals(found, ItemConverter.toDTO(items));
    }


    @Test
    public void shouldReturnEmptyArrayIfNoResultOnFilterByLocationId() {
        // When
        ItemService service = new ItemService(itemRepository);
        List<ItemDTO> found = service.filterByLocationId(5L);

        // Then
        Assertions.assertEquals(found, new ArrayList<>());
    }


    @Test
    public void shouldDeleteItemSuccess() {
        // Given
        ItemService service = new ItemService(itemRepository);
        when(itemRepository.existsById(5L)).thenReturn(true);

        // When
        service.delete(5L);

        // Then
        verify(itemRepository, times(1)).deleteById(5L);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionIfDeleteUnknownItem() {
        // Given
        ItemService service = new ItemService(itemRepository);
        when(itemRepository.existsById(5L)).thenReturn(false);

        // Then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(5L));
        verify(itemRepository, never()).deleteById(anyLong());
    }
}
