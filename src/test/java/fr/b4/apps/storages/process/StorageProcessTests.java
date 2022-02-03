package fr.b4.apps.storages.process;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.exceptions.ResourceNotSavedException;
import fr.b4.apps.storages.dto.BucketDTO;
import fr.b4.apps.storages.dto.StorageDTO;
import fr.b4.apps.storages.entities.Bucket;
import fr.b4.apps.storages.entities.Storage;
import fr.b4.apps.storages.repositories.BucketRepository;
import fr.b4.apps.storages.repositories.StoragesRepository;
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
public class StorageProcessTests {
    @Mock
    BucketRepository bucketRepository;

    @Mock
    StoragesRepository storagesRepository;

    @Test
    public void shouldSaveStorageSuccess() {
        // Given
        List<BucketDTO> bucketDTOS = DataGenerator.generateBucketDTO(5);
        bucketDTOS.forEach(bucketDTO -> bucketDTO.setId(null));
        StorageDTO storageDTO = DataGenerator.generateStoreDTO();
        storageDTO.setId(null);
        storageDTO.setBuckets(bucketDTOS);

        when(storagesRepository.save(any())).then((Answer<Storage>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((Storage) args[0]).setId(6L);
            return (Storage) args[0];
        });

        when(bucketRepository.saveAll(any())).then((Answer<List<Bucket>>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (List<Bucket>) args[0];
        });

        // When
        StorageProcess process = new StorageProcess(storagesRepository, bucketRepository);
        StorageDTO saved = process.save(storageDTO);

        // Then
        Assertions.assertEquals(6L, saved.getId());
        verify(storagesRepository, times(1)).save(any());
    }

    @Test
    public void shouldThrowExceptionForEmptyStorage() {
        // Given
        StorageProcess process = new StorageProcess(storagesRepository, bucketRepository);

        // When
        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> process.save(null));
    }

    @Test
    public void shouldThrowExceptionIfFailSaveStorage() {
        // Given
        when(storagesRepository.save(any())).thenReturn(null);
        StorageProcess process = new StorageProcess(storagesRepository, bucketRepository);
        StorageDTO storageDTO = DataGenerator.generateStoreDTO();

        // When
        // Then
        Assertions.assertThrows(ResourceNotSavedException.class, () -> process.save(storageDTO));
    }
}
