package fr.b4.apps.stores.web;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import fr.b4.apps.stores.dto.StoreDTO;
import fr.b4.apps.stores.services.StoresService;
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
public class StoresControllerTests {

    @Mock
    StoresService storesService;

    @Test
    public void shouldAddNewStoreSuccess() {
        // Given
        StoreDTO store = DataGenerator.generateStore();
        when(storesService.save(any())).then((Answer<StoreDTO>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (StoreDTO) args[0];
        });

        // When
        StoresController controller = new StoresController(storesService);
        StoreDTO saved = controller.addNewStore(store);

        // Then
        Assertions.assertEquals(saved, store);
    }

    @Test
    public void shouldNotCatchBadRequestException() {
        // Given
        doThrow(new BadRequestException("")).when(storesService).save(any());

        // Then
        StoresController controller = new StoresController(storesService);
        Assertions.assertThrows(BadRequestException.class, () -> controller.addNewStore(DataGenerator.generateStore()));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForMissingFields() {
        // Given
        StoresController controller = new StoresController(storesService);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.addNewStore(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.addNewStore(new StoreDTO()));
    }

    @Test
    public void shouldUpdateStoreSuccess() {
        // Given
        StoreDTO store = DataGenerator.generateStore();
        when(storesService.update(any())).then((Answer<StoreDTO>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (StoreDTO) args[0];
        });

        // When
        StoresController controller = new StoresController(storesService);
        StoreDTO saved = controller.updateStore(store);

        // Then
        Assertions.assertEquals(saved, store);
    }

    @Test
    public void shouldNotCatchBadRequestExceptionForUpdate() {
        // Given
        doThrow(new BadRequestException("")).when(storesService).save(any());

        // Then
        StoresController controller = new StoresController(storesService);
        Assertions.assertThrows(BadRequestException.class, () -> controller.addNewStore(DataGenerator.generateStore()));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForMissingFieldsForUpdate() {
        // Given
        StoresController controller = new StoresController(storesService);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.addNewStore(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.addNewStore(new StoreDTO()));
    }


    @Test
    public void getStoreWithIDSuccess() {
        // Given
        StoreDTO store = DataGenerator.generateStore();
        when(storesService.getByID(5L)).thenReturn(store);

        // When
        StoresController controller = new StoresController(storesService);
        StoreDTO found = controller.get(5L);
        Assertions.assertEquals(found, store);
    }

    @Test
    public void shouldNotCatchResourceNotFoundExceptionIfThrownByService() {
        // Given
        doThrow(new ResourceNotFoundException("")).when(storesService).getByID(anyLong());
        StoresController controller = new StoresController(storesService);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.get(5L));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIFIdNotValid() {
        // Given
        StoresController controller = new StoresController(storesService);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(-6L));
    }

    @Test
    public void getStoreWithUserIDSuccess() {
        // Given
        List<StoreDTO> stores = DataGenerator.generateStore(6);
        when(storesService.getByUserID(5L)).thenReturn(stores);

        // When
        StoresController controller = new StoresController(storesService);
        List<StoreDTO>  found = controller.getByUserID(5L);
        Assertions.assertEquals(found, stores);
    }

    @Test
    public void shouldNotCatchBadRequestExceptionIfThrownByService() {
        // Given
        doThrow(new BadRequestException("")).when(storesService).getByUserID(anyLong());
        StoresController controller = new StoresController(storesService);

        Assertions.assertThrows(BadRequestException.class, () -> controller.getByUserID(5L));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIFUserIdNotValid() {
        // Given
        StoresController controller = new StoresController(storesService);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getByUserID(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getByUserID(-6L));
    }

    @Test
    public void shouldDeleteStoreSuccess() {
        // Given
        StoresController controller = new StoresController(storesService);
        controller.delete(5L);

        verify(storesService, times(1)).delete(5L);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIFIdNotValidWhenDeletingStore() {
        // Given
        StoresController controller = new StoresController(storesService);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(-6L));
    }

    @Test
    public void shouldNotCatchResourceNotFoundExceptionIfThrownByServiceWhenDelete() {
        // Given
        doThrow(new ResourceNotFoundException("")).when(storesService).delete(anyLong());
        StoresController controller = new StoresController(storesService);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.delete(5L));
    }

}
