package fr.b4.apps.storages.process;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.services.ProductService;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import fr.b4.apps.storages.dto.ItemDTO;
import fr.b4.apps.storages.dto.UpdateQuantity;
import fr.b4.apps.storages.dto.UpdateQuantityDTO;
import fr.b4.apps.storages.entities.Item;
import fr.b4.apps.storages.repositories.BucketRepository;
import fr.b4.apps.storages.repositories.ItemRepository;
import fr.b4.apps.storages.repositories.UpdateQuantityRepository;
import org.hibernate.sql.Update;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemProcessTests {
    @Mock
    BucketRepository bucketRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ExpenseRepository expenseRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    ExpenseService expenseService;

    @Mock
    ProductService productService;

    @Mock
    UpdateQuantityRepository updateQuantityRepository;

    @Test
    public void shouldSaveItemWithExistingExpenseSuccess() {
        // Given
        ItemDTO dto = DataGenerator.generateItemDTO(false);
        when(bucketRepository.getById(any())).thenReturn(DataGenerator.generateBucket());
        when(userRepository.getById(any())).thenReturn(DataGenerator.generateUser());
        when(expenseRepository.getById(any())).thenReturn(DataGenerator.generateExpense());
        when(itemRepository.save(any())).then((Answer<Item>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((Item) args[0]).setId(5L);
            return (Item) args[0];
        });

        /// When
        ItemProcess process = new ItemProcess(bucketRepository,
                userRepository,
                expenseRepository,
                itemRepository,
                expenseService, productService, updateQuantityRepository);
        ItemDTO saved = process.save(dto);

        // Then
        Assertions.assertEquals(saved.getId(), 5L);
        verify(userRepository, times(1)).getById(any());
        verify(bucketRepository, times(1)).getById(any());
        verify(expenseRepository, times(1)).getById(any());
        verify(itemRepository, times(1)).save(any());
    }


    @Test
    public void shouldSaveItemWithNewExistingExpenseSuccess() {
        // Given
        ItemDTO dto = DataGenerator.generateItemDTO(false);
        dto.getExpense().setId(null);
        when(bucketRepository.getById(any())).thenReturn(DataGenerator.generateBucket());
        when(userRepository.getById(any())).thenReturn(DataGenerator.generateUser());
        when(itemRepository.save(any())).then((Answer<Item>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((Item) args[0]).setId(5L);
            return (Item) args[0];
        });

        when(expenseService.save(any())).then((Answer<Expense>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((ExpenseDTO) args[0]).setId(55L);
            return ExpenseConverter.toExpense((ExpenseDTO) args[0]);
        });

        /// When
        ItemProcess process = new ItemProcess(bucketRepository,
                userRepository,
                expenseRepository,
                itemRepository,
                expenseService, productService, updateQuantityRepository);
        ItemDTO saved = process.save(dto);

        // Then
        Assertions.assertEquals(saved.getId(), 5L);
        Assertions.assertEquals(saved.getExpense().getId(), 55L);
        verify(userRepository, times(1)).getById(any());
        verify(bucketRepository, times(1)).getById(any());
        verify(expenseRepository, never()).getById(any());
        verify(itemRepository, times(1)).save(any());
        verify(expenseService, times(1)).save(any());
    }

    @Test
    public void shouldThrowBadRequestExceptionIfExpenseUnknown() {
        // Given
        ItemDTO dto = DataGenerator.generateItemDTO(false);
        dto.getExpense().setId(5L);
        when(userRepository.getById(any())).thenReturn(DataGenerator.generateUser());
        when(bucketRepository.getById(any())).thenReturn(DataGenerator.generateBucket());

        // Then
        ItemProcess process = new ItemProcess(bucketRepository,
                userRepository,
                expenseRepository,
                itemRepository,
                expenseService, productService, updateQuantityRepository);
        Assertions.assertThrows(BadRequestException.class, () -> process.save(dto));
    }

    @Test
    public void shouldThrowBadRequestExceptionIfLocationUnknown() {
        // Given
        ItemDTO dto = DataGenerator.generateItemDTO(false);
        dto.getLocation().setId(5L);
        when(bucketRepository.getById(5L)).thenReturn(null);

        // Then
        ItemProcess process = new ItemProcess(bucketRepository,
                userRepository,
                expenseRepository,
                itemRepository,
                expenseService, productService, updateQuantityRepository);
        Assertions.assertThrows(BadRequestException.class, () -> process.save(dto));
    }

    @Test
    public void shouldThrowBadRequestExceptionIfAuthorUnknown() {
        // Given
        ItemDTO dto = DataGenerator.generateItemDTO(false);
        dto.getAuthor().setId(5L);
        when(userRepository.getById(5L)).thenReturn(null);
        when(bucketRepository.getById(any())).thenReturn(DataGenerator.generateBucket());

        // Then
        ItemProcess process = new ItemProcess(bucketRepository,
                userRepository,
                expenseRepository,
                itemRepository,
                expenseService, productService, updateQuantityRepository);
        Assertions.assertThrows(BadRequestException.class, () -> process.save(dto));
    }

    @Test
    public void shouldUpdateItemQuantitySuccess() {
        Item savedItem  = DataGenerator.generateItem(true);
        // Given
        when(itemRepository.getById(6L)).thenReturn(savedItem);
        when(updateQuantityRepository.save(any())).then((Answer<UpdateQuantity>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((UpdateQuantity) args[0]).setId(5L);
            return (UpdateQuantity) args[0];
        });

        // When
        ItemProcess process = new ItemProcess(bucketRepository,
                userRepository,
                expenseRepository,
                itemRepository,
                expenseService, productService, updateQuantityRepository);
        UpdateQuantityDTO rq = new UpdateQuantityDTO();
        rq.setQuantity(93.6f);
        rq.setComment("comment");

        UpdateQuantityDTO rp = process.updateQuantity(6L, rq);

        // Then
        Assertions.assertEquals(rp.getId(), 5L);
        Assertions.assertEquals(savedItem.getRemaining(), rq.getQuantity());
        verify(itemRepository, times(1)).save(any());
        verify(updateQuantityRepository, times(1)).save(any());
    }
}
