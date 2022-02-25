package fr.b4.apps.storages.web;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.exceptions.ForbiddenException;
import fr.b4.apps.storages.dto.ItemDTO;

import fr.b4.apps.storages.dto.UpdateQuantity;
import fr.b4.apps.storages.dto.UpdateQuantityDTO;
import fr.b4.apps.storages.process.ItemProcess;
import fr.b4.apps.storages.services.ItemService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTests {

    @Mock
    ItemService itemService;

    @Mock
    ItemProcess itemProcess;

    @Test
    public void shouldSaveValidItemSuccess() {
        // Given
        ItemDTO item = DataGenerator.generateItemDTO(false);
        when(itemProcess.save(any())).then((Answer<ItemDTO>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((ItemDTO) args[0]).setId(5L);
            return (ItemDTO) args[0];
        });

        // When
        ItemController controller = new ItemController(itemService, itemProcess);
        ItemDTO saved = controller.save(item);

        // Then
        Assertions.assertFalse(ObjectUtils.isEmpty(saved));
        Assertions.assertEquals(item.getLocation().getName(), saved.getLocation().getName());
        Assertions.assertEquals(5L, saved.getId());
    }


    @Test
    public void shouldThrowIllegalArgumentExceptionIfSaveValidItem() {
        // Given
        ItemDTO item1 = DataGenerator.generateItemDTO(false);
        item1.setLocation(null);

        ItemDTO item2 = DataGenerator.generateItemDTO(false);
        item2.setAuthor(null);

        ItemDTO item3 = DataGenerator.generateItemDTO(true);
        // When
        ItemController controller = new ItemController(itemService, itemProcess);
        // required location missing
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.save(item1));
        // required author missing
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.save(item2));
        // id must be null
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.save(item3));

    }

    @Test
    public void shouldUpdateItemSuccess() {
        // Given
        ItemDTO item = DataGenerator.generateItemDTO(true);
        when(itemService.update(any())).then((Answer<ItemDTO>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (ItemDTO) args[0];
        });

        // When
        ItemController controller = new ItemController(itemService, itemProcess);
        ItemDTO saved = controller.update(item);

        // Then
        Assertions.assertFalse(ObjectUtils.isEmpty(saved));
        verify(itemService, times(1)).update(item);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfUpdateValidItem() {
        // Given
        ItemDTO item1 = DataGenerator.generateItemDTO(true);
        item1.setLocation(null);

        ItemDTO item2 = DataGenerator.generateItemDTO(true);
        item2.setAuthor(null);

        ItemDTO item3 = DataGenerator.generateItemDTO(false);

        // When
        ItemController controller = new ItemController(itemService, itemProcess);
        // required location missing
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.update(item1));
        // required author missing
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.update(item2));
        // id must not be null
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.update(item3));

    }

    @Test
    public void shouldThrowExceptionIfGetWithInvalidId() {
        // Given
        ItemController controller = new ItemController(itemService, itemProcess);

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(-5L, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(null, -6L));
        Assertions.assertDoesNotThrow(() -> controller.get(5L, null));
        Assertions.assertDoesNotThrow(() -> controller.get(null, 9L));
    }

    @Test
    public void shouldFilterByBucketSuccess() {
        // Given
        List<ItemDTO> items = DataGenerator.generateItemDTO(6);
        when(itemService.filterByLocationId(5L)).thenReturn(items);

        // WHen
        ItemController controller = new ItemController(itemService, itemProcess);
        List<ItemDTO> found = controller.get(null, 5L);

        // Then
        Assertions.assertEquals(found, items);
    }


    @Test
    public void shouldFilterByUserSuccess() {
        // Given
        List<ItemDTO> items = DataGenerator.generateItemDTO(6);
        when(itemService.filterByAuthorId(5L)).thenReturn(items);

        // WHen
        ItemController controller = new ItemController(itemService, itemProcess);
        List<ItemDTO> found = controller.get(5L, null);

        // Then
        Assertions.assertEquals(found, items);
    }


    @Test
    public void shouldDeleteItemSuccess() {
        // Given
        ItemController controller = new ItemController(itemService, itemProcess);

        // Then
        controller.delete(45L);
        verify(itemService, times(1)).delete(anyLong());
    }

    @Test
    public void shouldThrowExceptionIfDeleteWithInvalidId() {
        // Given
        ItemController controller = new ItemController(itemService, itemProcess);

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.delete(-4L));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.delete(null));
    }

    @Test
    public void shouldUpdateItemQuantitySuccess() {
        // Given
        UpdateQuantityDTO rq = new UpdateQuantityDTO();
        rq.setQuantity(8.6f);

        when(itemProcess.updateQuantity(any(), any())).then((Answer<UpdateQuantityDTO>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((UpdateQuantityDTO) args[1]).setId(5L);
            return (UpdateQuantityDTO) args[1];
        });

        // When
        ItemController controller = new ItemController(itemService, itemProcess);
        UpdateQuantityDTO saved = controller.updateQuantity(9L, rq);

        // Then
        Assertions.assertEquals(saved.getId(), 5L);

    }
}
