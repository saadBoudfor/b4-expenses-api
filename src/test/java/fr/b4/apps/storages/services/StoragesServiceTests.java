package fr.b4.apps.storages.services;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.storages.dto.StorageDTO;
import fr.b4.apps.storages.entities.Storage;
import fr.b4.apps.storages.repositories.StoragesRepository;
import fr.b4.apps.storages.util.converters.StoragesConverters;
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
public class StoragesServiceTests {
    @Mock
    StoragesRepository storagesRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void shouldSaveStoreSuccess() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();
        store.setId(null); // new store has not defined id

        when(storagesRepository.save(any())).then((Answer<Storage>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (Storage) args[0];
        });

        when(userRepository.existsById(any())).thenReturn(true);

        // When
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        StorageDTO saved = service.save(store);

        // Then
        Assertions.assertEquals(saved, store);
    }

    @Test
    public void shouldThrowBadRequestExceptionIfSaveStoreForUnKnownUser() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();
        store.setId(null); // new store has not defined id

        // Then
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.save(store));
    }

    @Test
    public void shouldThrowBadRequestExceptionIfSaveStoreWithExistingId() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();

        // Then
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.save(store));

    }


    @Test
    public void shouldThrowBadRequestExceptionIfSaveNamelessStore() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();
        store.setId(null); // new store has not defined id
        store.setName(null); // nameless store !

        // Then
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.save(store));
    }


    @Test
    public void shouldThrowBadRequestExceptionIfSaveWithMissingOwner() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();
        store.setId(null);
        store.setOwner(null); // nameless store !

        // Then
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.save(store));
    }

    @Test
    public void shouldUpdateStoreSuccess() {
        // Given
        Storage storage = DataGenerator.generateStore();
        StorageDTO storageDTO = StoragesConverters.toDTO(storage);

        when(storagesRepository.getById(storage.getId())).thenReturn(storage);

        when(storagesRepository.save(any())).then((Answer<Storage>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (Storage) args[0];
        });

        // When
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        storageDTO.setName("ok");
        StorageDTO saved = service.update(storageDTO);

        // Then
        Assertions.assertEquals(saved.getName(), "ok");
    }

    @Test
    public void shouldThrowBadRequestExceptionIfUpdateStoreWithoutId() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();
        store.setId(null); // new store has not defined id

        // Then
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.update(store));

    }

    @Test
    public void shouldThrowResourceNotFoundExceptionIfUpdateUnknownStore() {
        // Given
        StorageDTO store = DataGenerator.generateStoreDTO();
        when(storagesRepository.getById(store.getId())).thenReturn(null);

        // Then
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(store));

    }

    @Test
    public void shouldGetStoreByIdSuccess() {
        Storage storage = DataGenerator.generateStore();
        storage.setId(5L);
        when(storagesRepository.getById(5L)).thenReturn(storage);

        StoragesService service = new StoragesService(storagesRepository, userRepository);
        StorageDTO found = service.getByID(5L);

        Assertions.assertEquals(found, StoragesConverters.toDTO(storage));
    }

    @Test
    public void shouldThrowResourceNotFoundExpenseIfNoResult() {
        when(storagesRepository.getById(5L)).thenReturn(null);
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.getByID(5L));
    }

    @Test
    public void shouldGetStoreByUserIDSuccess() {
        // When
        when(userRepository.existsById(6L)).thenReturn(true);
        List<Storage> storageList = DataGenerator.generateStore(9);
        when(storagesRepository.getByOwnerId(6L)).thenReturn(storageList);

        // When
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        List<StorageDTO> found = service.getByUserID(6L);

        // Then
        Assertions.assertEquals(found.size(), storageList.size());
    }

    @Test
    public void shouldThrowBadRequestIfUserUnknownForStoreSearch() {
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        Assertions.assertThrows(BadRequestException.class, () -> service.getByUserID(5L));
    }

    @Test
    public void shouldDeleteSuccess() {
        when(storagesRepository.existsById(5L)).thenReturn(true);

        StoragesService service = new StoragesService(storagesRepository, userRepository);
        service.delete(5L);

        verify(storagesRepository, Mockito.times(1)).deleteById(5L);
    }

    @Test
    public void shouldThrowNotFoundExceptionIFIdUnknownWhenDelete() {
        StoragesService service = new StoragesService(storagesRepository, userRepository);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(5L));
    }
}
