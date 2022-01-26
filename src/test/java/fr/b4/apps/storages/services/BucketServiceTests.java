package fr.b4.apps.storages.services;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.storages.dto.BucketDTO;
import fr.b4.apps.storages.entities.Bucket;
import fr.b4.apps.storages.repositories.BucketRepository;
import fr.b4.apps.storages.util.converters.BucketConverter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BucketServiceTests {

    @Mock
    BucketRepository bucketRepository;

    @Test
    public void shouldSaveSuccess() {
        // Given
        BucketDTO dto = DataGenerator.generateBucketDTO();
        when(bucketRepository.save(any())).thenReturn(BucketConverter.toBucket(dto));

        // When
        BucketService bucketService = new BucketService(bucketRepository);
        BucketDTO found = bucketService.save(dto);

        // Then
        verify(bucketRepository, times(1)).save(any());
        Assertions.assertEquals(found.getName(), dto.getName());
    }

    @Test
    public void shouldUpdateSuccess() {
        // Given
        BucketDTO dto = DataGenerator.generateBucketDTO();
        when(bucketRepository.save(any())).then((Answer<Bucket>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (Bucket) args[0];
        });

        when(bucketRepository.getById(anyLong())).thenReturn(BucketConverter.toBucket(dto));


        // When
        BucketService bucketService = new BucketService(bucketRepository);

        dto.setName("ok");
        BucketDTO found = bucketService.update(dto);

        // Then
        verify(bucketRepository, times(1)).save(any());
        Assertions.assertEquals(found.getId(), dto.getId());
        Assertions.assertEquals(found.getName(), "ok");
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionIfUpdateUnknownBucket() {
        // Given
        BucketDTO dto = DataGenerator.generateBucketDTO();
        when(bucketRepository.getById(anyLong())).thenReturn(null);

        // When
        BucketService bucketService = new BucketService(bucketRepository);

        // Then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> bucketService.update(dto));
    }

    @Test
    public void shouldDeleteBucketSuccess() {
        // Given
        BucketService bucketService = new BucketService(bucketRepository);
        when(bucketRepository.existsById(5L)).thenReturn(true);
        // When
        bucketService.delete(5L);

        // Then
        verify(bucketRepository, times(1)).deleteById(5L);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionIfDeleteUnknownBucket() {
        // Given
        BucketService bucketService = new BucketService(bucketRepository);
        when(bucketRepository.existsById(5L)).thenReturn(false);

        // Then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> bucketService.delete(5L));
        verify(bucketRepository, never()).deleteById(anyLong());
    }

    @Test
    public void shouldFilterBucketsByStoreIdSuccess() {
        // Given
        List<Bucket> buckets = DataGenerator.generateBucket(6);
        when(bucketRepository.findByStoreId(5L)).thenReturn(buckets);

        // When
        BucketService bucketService = new BucketService(bucketRepository);
        List<BucketDTO> found = bucketService.filterByStoreId(5L);

        // Then
        Assertions.assertEquals(found, BucketConverter.toDTO(buckets));
    }


    @Test
    public void shouldFilterBucketsByOwnerIdSuccess() {
        // Given
        List<Bucket> buckets = DataGenerator.generateBucket(6);
        when(bucketRepository.findByOwnerId(5L)).thenReturn(buckets);

        // When
        BucketService bucketService = new BucketService(bucketRepository);
        List<BucketDTO> found = bucketService.filterByUserId(5L);

        // Then
        Assertions.assertEquals(found, BucketConverter.toDTO(buckets));
    }

    @Test
    public void shouldReturnEmptyArrayIfNoResultOnFilterByStoreId() {
        // When
        BucketService bucketService = new BucketService(bucketRepository);
        List<BucketDTO> found = bucketService.filterByStoreId(5L);

        // Then
        Assertions.assertEquals(found, new ArrayList<>());
    }


    @Test
    public void shouldReturnEmptyArrayIfNoResultOnFilterByUserId() {
        // When
        BucketService bucketService = new BucketService(bucketRepository);
        List<BucketDTO> found = bucketService.filterByUserId(5L);

        // Then
        Assertions.assertEquals(found, new ArrayList<>());
    }
}
