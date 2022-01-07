package fr.b4.apps.stores.services;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.stores.dto.StoreDTO;
import fr.b4.apps.stores.entities.Store;
import fr.b4.apps.stores.repositories.StoreRepository;
import fr.b4.apps.stores.util.converters.StoreConverters;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StoresServiceTests {
    @Mock
    StoreRepository storeRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void shouldSaveStoreSuccess() {
        // Given
        StoreDTO store = DataGenerator.generateStoreDTO();
        store.setId(null); // new store has not defined id

        when(storeRepository.save(any())).then((Answer<Store>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (Store) args[0];
        });

        when(userRepository.existsById(any())).thenReturn(true);

        // When
        StoresService service = new StoresService(storeRepository, userRepository);
        StoreDTO saved = service.save(store);

        // Then
        Assertions.assertEquals(saved, store);
    }

    @Test
    public void shouldThrowBadRequestExceptionIfSaveStoreForUnKnownUser() {
        // Given
        StoreDTO store = DataGenerator.generateStoreDTO();
        store.setId(null); // new store has not defined id

        // Then
        StoresService service = new StoresService(storeRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.save(store));
    }

    @Test
    public void shouldThrowBadRequestExceptionIfSaveStoreWithExistingId() {
        // Given
        StoreDTO store = DataGenerator.generateStoreDTO();

        // Then
        StoresService service = new StoresService(storeRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.save(store));

    }


    @Test
    public void shouldThrowBadRequestExceptionIfSaveNamelessStore() {
        // Given
        StoreDTO store = DataGenerator.generateStoreDTO();
        store.setId(null); // new store has not defined id
        store.setName(null); // nameless store !

        // Then
        StoresService service = new StoresService(storeRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.save(store));
    }


    @Test
    public void shouldThrowBadRequestExceptionIfSaveWithMissingOwner() {
        // Given
        StoreDTO store = DataGenerator.generateStoreDTO();
        store.setId(null);
        store.setOwner(null); // nameless store !

        // Then
        StoresService service = new StoresService(storeRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.save(store));
    }

    @Test
    public void shouldUpdateStoreSuccess() {
        // Given
        Store store = DataGenerator.generateStore();
        StoreDTO storeDTO = StoreConverters.toDTO(store);

        when(storeRepository.getById(store.getId())).thenReturn(store);

        when(storeRepository.save(any())).then((Answer<Store>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (Store) args[0];
        });

        // When
        StoresService service = new StoresService(storeRepository, userRepository);
        storeDTO.setName("ok");
        StoreDTO saved = service.update(storeDTO);

        // Then
        Assertions.assertEquals(saved.getName(), "ok");
    }

    @Test
    public void shouldThrowBadRequestExceptionIfUpdateStoreWithoutId() {
        // Given
        StoreDTO store = DataGenerator.generateStoreDTO();
        store.setId(null); // new store has not defined id

        // Then
        StoresService service = new StoresService(storeRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.update(store));

    }

    @Test
    public void shouldThrowResourceNotFoundExceptionIfUpdateUnknownStore() {
        // Given
        StoreDTO store = DataGenerator.generateStoreDTO();
        when(storeRepository.getById(store.getId())).thenReturn(null);

        // Then
        StoresService service = new StoresService(storeRepository, userRepository);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(store));

    }

    @Test
    public void shouldGetStoreByIdSuccess() {
        Store store = DataGenerator.generateStore();
        store.setId(5L);
        when(storeRepository.getById(5L)).thenReturn(store);

        StoresService service = new StoresService(storeRepository, userRepository);
        StoreDTO found = service.getByID(5L);

        Assertions.assertEquals(found, StoreConverters.toDTO(store));
    }

    @Test
    public void shouldThrowResourceNotFoundExpenseIfNoResult() {
        when(storeRepository.getById(5L)).thenReturn(null);
        StoresService service = new StoresService(storeRepository, userRepository);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.getByID(5L));
    }

    @Test
    public void shouldGetStoreByUserIDSuccess() {
        // When
        when(userRepository.existsById(6L)).thenReturn(true);
        List<Store> storeList = DataGenerator.generateStore(9);
        when(storeRepository.getByOwnerId(6L)).thenReturn(storeList);

        // When
        StoresService service = new StoresService(storeRepository, userRepository);
        List<StoreDTO> found = service.getByUserID(6L);

        // Then
        Assertions.assertEquals(found.size(), storeList.size());
    }

    @Test
    public void shouldThrowBadRequestIfUserUnknownForStoreSearch() {
        StoresService service = new StoresService(storeRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.getByUserID(5L));
    }

    @Test
    public void shouldDeleteSuccess() {
        when(storeRepository.existsById(5L)).thenReturn(true);

        StoresService service = new StoresService(storeRepository, userRepository);
        service.delete(5L);

        verify(storeRepository, Mockito.times(1)).deleteById(5L);
    }

    @Test
    public void shouldThrowNotFoundExceptionIFIdUnknownWhenDelete() {
        StoresService service = new StoresService(storeRepository, userRepository);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(5L));
    }
}
