package fr.b4.apps.storages.web;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.expenses.dto.MessageDTO;
import fr.b4.apps.storages.dto.BucketDTO;
import fr.b4.apps.storages.services.BucketService;
import org.apache.commons.lang3.ObjectUtils;
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
public class BucketControllerTests {

    @Mock
    BucketService bucketService;


    @Test
    public void shouldAddBucketSuccess() {
        // Given
        BucketDTO dto = DataGenerator.generateBucketDTO();
        dto.setId(null);

        when(bucketService.save(any())).then((Answer<BucketDTO>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((BucketDTO) args[0]).setId(5L);
            return (BucketDTO) args[0];
        });

        // When
        BucketController controller = new BucketController(bucketService);
        BucketDTO found = controller.addBucket(dto);

        // Then
        Assertions.assertFalse(ObjectUtils.isEmpty(found.getId()));
        Assertions.assertEquals(found.getName(), dto.getName());
        verify(bucketService, times(1)).save(any());
    }

    @Test
    public void shouldThrowBadRequestExceptionIfRequiredFieldMissing() {
        // Given
        BucketDTO dto1 = DataGenerator.generateBucketDTO();
        dto1.setId(null);
        dto1.setOwner(null);

        BucketDTO dto2 = DataGenerator.generateBucketDTO();
        dto2.setId(null);
        dto2.setName(null);

        BucketDTO dto3 = DataGenerator.generateBucketDTO();
        dto3.setId(null);
        dto3.setStorage(null);

        BucketDTO dto4 = DataGenerator.generateBucketDTO();
        dto4.setId(null);
        dto4.getOwner().setId(null);

        BucketDTO dto5 = DataGenerator.generateBucketDTO();
        dto5.setId(null);
        dto5.getStorage().setId(null);


        // Then
        BucketController controller = new BucketController(bucketService);
        Assertions.assertThrows(BadRequestException.class, () -> controller.addBucket(dto1));
        Assertions.assertThrows(BadRequestException.class, () -> controller.addBucket(dto2));
        Assertions.assertThrows(BadRequestException.class, () -> controller.addBucket(dto3));
        Assertions.assertThrows(BadRequestException.class, () -> controller.addBucket(dto4));
        Assertions.assertThrows(BadRequestException.class, () -> controller.addBucket(dto5));
    }

    @Test
    public void shouldThrowExceptionIfAddBackedWithId() {
        BucketDTO dto = DataGenerator.generateBucketDTO();

        // Then
        BucketController controller = new BucketController(bucketService);
        Assertions.assertThrows(BadRequestException.class, () -> controller.addBucket(dto));
    }


    @Test
    public void shouldUpdateBucketSuccess() {
        // Given
        BucketDTO dto = DataGenerator.generateBucketDTO();

        when(bucketService.update(any())).then((Answer<BucketDTO>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (BucketDTO) args[0];
        });

        // When
        BucketController controller = new BucketController(bucketService);
        BucketDTO found = controller.update(dto);

        // Then
        Assertions.assertFalse(ObjectUtils.isEmpty(found));
        verify(bucketService, times(1)).update(any());
    }


    @Test
    public void shouldThrowBadRequestExceptionIfUpdateWithRequiredFieldMissing() {
        // Given
        BucketDTO dto1 = DataGenerator.generateBucketDTO();
        dto1.setId(null);
        dto1.setOwner(null);

        BucketDTO dto2 = DataGenerator.generateBucketDTO();
        dto2.setId(null);
        dto2.setName(null);

        BucketDTO dto3 = DataGenerator.generateBucketDTO();
        dto3.setId(null);
        dto3.setStorage(null);

        BucketDTO dto4 = DataGenerator.generateBucketDTO();
        dto4.setId(null);
        dto4.getOwner().setId(null);

        BucketDTO dto5 = DataGenerator.generateBucketDTO();
        dto5.setId(null);
        dto5.getStorage().setId(null);


        // Then
        BucketController controller = new BucketController(bucketService);
        Assertions.assertThrows(BadRequestException.class, () -> controller.update(dto1));
        Assertions.assertThrows(BadRequestException.class, () -> controller.update(dto2));
        Assertions.assertThrows(BadRequestException.class, () -> controller.update(dto3));
        Assertions.assertThrows(BadRequestException.class, () -> controller.update(dto4));
        Assertions.assertThrows(BadRequestException.class, () -> controller.update(dto5));
    }


    @Test
    public void shouldThrowExceptionIfAddUpdateBackedWithNullId() {
        BucketDTO dto = DataGenerator.generateBucketDTO();
        dto.setId(null);

        // Then
        BucketController controller = new BucketController(bucketService);
        Assertions.assertThrows(BadRequestException.class, () -> controller.update(dto));
    }

    @Test
    public void shouldDeleteBucketSuccess() {
        // Given
        BucketController controller = new BucketController(bucketService);

        // Then
        controller.delete(45L);
        verify(bucketService, times(1)).delete(anyLong());
    }

    @Test
    public void shouldThrowExceptionIfDeleteWithInvalidId() {
        // Given
        BucketController controller = new BucketController(bucketService);

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.delete(-4L));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.delete(null));
    }

    @Test
    public void shouldThrowExceptionIfGetWithInvalidId() {
        // Given
        BucketController controller = new BucketController(bucketService);

        //Then
        Assertions.assertThrows(BadRequestException.class, () -> controller.get(null, null));
        Assertions.assertThrows(BadRequestException.class, () -> controller.get(null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(-5L, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.get(null, -6L));
        Assertions.assertDoesNotThrow(() -> controller.get(5L, null));
        Assertions.assertDoesNotThrow(() -> controller.get(null, 9L));
    }

    @Test
    public void shouldFilterByStoreSuccess() {
        // Given
        List<BucketDTO> buckets = DataGenerator.generateBucketDTO(6);
        when(bucketService.filterByStoreId(5L)).thenReturn(buckets);

        // WHen
        BucketController controller = new BucketController(bucketService);
        List<BucketDTO> found = controller.get(5L, null);

        // Then
        Assertions.assertEquals(found, buckets);
    }


    @Test
    public void shouldFilterByUserSuccess() {
        // Given
        List<BucketDTO> buckets = DataGenerator.generateBucketDTO(6);
        when(bucketService.filterByUserId(5L)).thenReturn(buckets);

        // When
        BucketController controller = new BucketController(bucketService);
        List<BucketDTO> found = controller.get(null, 5L);

        // Then
        Assertions.assertEquals(found, buckets);
    }


    @Test
    public void shouldCheckIfNameUsedSuccess() {
        when(bucketService.isNameUsed("ok", 3L)).thenReturn(new MessageDTO(true));

        // When
        BucketController controller = new BucketController(bucketService);
        MessageDTO dto = controller.isNameUsed("ok", 3L);

        // Then
        Assertions.assertEquals(dto, new MessageDTO(true));
    }

}
