package fr.b4.apps.storages.web;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.expenses.dto.MessageDTO;
import fr.b4.apps.storages.dto.StorageDTO;
import fr.b4.apps.storages.process.StorageProcess;
import fr.b4.apps.storages.services.StoragesService;
import fr.b4.apps.storages.util.converters.BucketConverter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StoragesControllerTests {

    @Mock
    StoragesService storagesService;

    @Mock
    StorageProcess storageProcess;

    @Test
    public void shouldAddNewStoreSuccess() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();
        store.setBuckets(BucketConverter.toDTO(DataGenerator.generateBucket(1)));
        when(storageProcess.save(any())).then((Answer<StorageDTO>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (StorageDTO) args[0];
        });

        // When
        StoragesController controller = new StoragesController(storagesService, storageProcess);
        StorageDTO saved = controller.addNewStore(store);

        // Then
        Assertions.assertEquals(saved, store);
    }

    @Test
    public void shouldNotCatchBadRequestException() {
        // Given
//        doThrow(new BadRequestException("")).when(storagesService).save(any());

        // Then
        StoragesController controller = new StoragesController(storagesService, storageProcess);
        Assertions.assertThrows(BadRequestException.class, () -> controller.addNewStore(DataGenerator.generateStoreDTO()));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForMissingFields() {
        // Given
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        // Then
        Assertions.assertThrows(BadRequestException.class, () -> controller.addNewStore(null));
        Assertions.assertThrows(BadRequestException.class, () -> controller.addNewStore(new StorageDTO()));
    }

    @Test
    public void shouldUpdateStoreSuccess() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();
        when(storagesService.update(any())).then((Answer<StorageDTO>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (StorageDTO) args[0];
        });

        // When
        StoragesController controller = new StoragesController(storagesService, storageProcess);
        StorageDTO saved = controller.updateStore(store);

        // Then
        Assertions.assertEquals(saved, store);
    }

    @Test
    public void shouldNotCatchBadRequestExceptionForUpdate() {
        // Given
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        // Then
        Assertions.assertThrows(BadRequestException.class, () -> controller.addNewStore(DataGenerator.generateStoreDTO()));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForMissingFieldsForUpdate() {
        // Given
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        // Then
        Assertions.assertThrows(BadRequestException.class, () -> controller.addNewStore(null));
        Assertions.assertThrows(BadRequestException.class, () -> controller.addNewStore(new StorageDTO()));
    }


    @Test
    public void getStoreWithIDSuccess() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();
        when(storagesService.getByID(5L)).thenReturn(store);

        // When
        StoragesController controller = new StoragesController(storagesService, storageProcess);
        StorageDTO found = controller.get(5L);
        Assertions.assertEquals(found, store);
    }

    @Test
    public void shouldNotCatchResourceNotFoundExceptionIfThrownByService() {
        // Given
        doThrow(new ResourceNotFoundException("")).when(storagesService).getByID(anyLong());
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.get(5L));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIFIdNotValid() {
        // Given
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(-6L));
    }

    @Test
    public void getStoreWithUserIDSuccess() {
        // Given
        List<StorageDTO> stores = DataGenerator.generateStoreDTO(6);
        when(storagesService.getByUserID(5L)).thenReturn(stores);

        // When
        StoragesController controller = new StoragesController(storagesService, storageProcess);
        List<StorageDTO> found = controller.getByUserID(5L);
        Assertions.assertEquals(found, stores);
    }

    @Test
    public void shouldNotCatchBadRequestExceptionIfThrownByService() {
        // Given
        doThrow(new BadRequestException("")).when(storagesService).getByUserID(anyLong());
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        Assertions.assertThrows(BadRequestException.class, () -> controller.getByUserID(5L));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIFUserIdNotValid() {
        // Given
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getByUserID(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getByUserID(-6L));
    }

    @Test
    public void shouldDeleteStoreSuccess() {
        // Given
        StoragesController controller = new StoragesController(storagesService, storageProcess);
        controller.delete(5L);

        verify(storagesService, times(1)).delete(5L);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIFIdNotValidWhenDeletingStore() {
        // Given
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(-6L));
    }

    @Test
    public void shouldNotCatchResourceNotFoundExceptionIfThrownByServiceWhenDelete() {
        // Given
        doThrow(new ResourceNotFoundException("")).when(storagesService).delete(anyLong());
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.delete(5L));
    }


    @Test
    public void shouldReturnIfNameIsUsed() {
        // Given
        when(storagesService.existByName("ok")).thenReturn(true);
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        // when
        MessageDTO response = controller.existByName("ok");

        // then
        Assertions.assertEquals(new MessageDTO(true), response);
    }

    @Test
    public void shouldThrowExceptionIfSearchingEmptyName() {
        // Given
        StoragesController controller = new StoragesController(storagesService, storageProcess);

        // when - then
        MessageDTO response = controller.existByName("ok");
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.existByName(""));
    }
}
