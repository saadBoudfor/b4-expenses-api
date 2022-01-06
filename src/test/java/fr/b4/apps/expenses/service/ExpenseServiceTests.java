package fr.b4.apps.expenses.service;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.AddressRepository;
import fr.b4.apps.common.repositories.PlaceRepository;
import fr.b4.apps.common.repositories.ProductRepository;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.dto.NutrientStatRecapDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.repositories.ExpenseLineRepository;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static fr.b4.apps.expenses.util.ExpenseUtils.getFistDayOfCurrentMonth;
import static fr.b4.apps.expenses.util.ExpenseUtils.getFistDayOfCurrentWeek;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseServiceTests {
    @Mock
    ExpenseRepository expenseRepository;
    @Mock
    ExpenseLineRepository expenseLineRepository;
    @Mock
    PlaceRepository placeRepository;
    @Mock
    AddressRepository addressRepository;
    @Mock
    ProductRepository productRepository;

    @Test
    public void shouldGetNutrientStatsSuccess() {
        // Given
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{"label", BigInteger.valueOf(69L)});
        data.add(new Object[]{"label1", BigInteger.valueOf(79L)});
        data.add(new Object[]{"label2", BigInteger.valueOf(89L)});
        when(expenseLineRepository.getNutrientStats()).thenReturn(data);
        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);
        NutrientStatRecapDTO res = expenseService.getNutrientStats();

        // Then
        Assertions.assertEquals(res.getStats().get(0).getCount(), "69");
        Assertions.assertEquals(res.getStats().get(1).getCount(), "79");
        Assertions.assertEquals(res.getStats().get(2).getCount(), "89");

        Assertions.assertEquals(res.getStats().get(0).getLabel(), "label");
        Assertions.assertEquals(res.getStats().get(1).getLabel(), "label1");
        Assertions.assertEquals(res.getStats().get(2).getLabel(), "label2");
    }

    @Test
    public void shouldReturnEmptyArrayIfNoStats() {
        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);
        NutrientStatRecapDTO res = expenseService.getNutrientStats();

        // Then
        Assertions.assertFalse(ObjectUtils.isEmpty(res));
        Assertions.assertTrue(CollectionUtils.isEmpty(res.getStats()));
    }

    @Test
    public void shouldGetExpenseStatsFilteredByPlaceSuccess() {
        // Given
        final LocalDate firstDayOfCurrentWeek = getFistDayOfCurrentWeek();
        final LocalDate firstDayOfCurrentMonth = getFistDayOfCurrentMonth();

        when(expenseRepository.sumAllExpenses(99L, firstDayOfCurrentMonth, PlaceType.STORE.toString()))
                .thenReturn(160.66f);
        when(expenseRepository.countAllExpenses(99L, firstDayOfCurrentMonth, PlaceType.STORE.toString()))
                .thenReturn(22);

        when(expenseRepository.sumAllExpenses(99L, firstDayOfCurrentWeek, PlaceType.STORE.toString()))
                .thenReturn(10.6f);
        when(expenseRepository.countAllExpenses(99L, firstDayOfCurrentWeek, PlaceType.STORE.toString()))
                .thenReturn(2);

        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);
        ExpenseBasicStatsDTO res = expenseService.getExpenseStats(99L, PlaceType.STORE);

        // Then
        Assertions.assertEquals(res.getCount(), 22);
        Assertions.assertEquals(res.getTotal(), 160.66f);
        Assertions.assertEquals(res.getCountForCurrentWeek(), 2);
        Assertions.assertEquals(res.getTotalForCurrentWeek(), 10.6f);
    }

    @Test
    public void shouldGetExpenseStatsSuccess() {
        // Given
        final LocalDate firstDayOfCurrentWeek = getFistDayOfCurrentWeek();
        final LocalDate firstDayOfCurrentMonth = getFistDayOfCurrentMonth();

        when(expenseRepository.sumAllExpenses(99L, firstDayOfCurrentMonth))
                .thenReturn(160.66f);
        when(expenseRepository.countAllExpenses(99L, firstDayOfCurrentMonth))
                .thenReturn(22);

        when(expenseRepository.sumAllExpenses(99L, firstDayOfCurrentWeek))
                .thenReturn(10.6f);
        when(expenseRepository.countAllExpenses(99L, firstDayOfCurrentWeek))
                .thenReturn(2);


        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);
        ExpenseBasicStatsDTO res = expenseService.getExpenseStats(99L);

        // Then
        Assertions.assertEquals(res.getCount(), 22);
        Assertions.assertEquals(res.getTotal(), 160.66f);
        Assertions.assertEquals(res.getCountForCurrentWeek(), 2);
        Assertions.assertEquals(res.getTotalForCurrentWeek(), 10.6f);
    }

    @Test
    public void shouldDeleteSuccess() {
        // Given
        when(expenseLineRepository.deleteAllByExpenseId(9L)).thenReturn(9);

        // when
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);
        expenseService.delete(9L);

        // Then
        verify(expenseRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void shouldDelete() {
        when(expenseLineRepository.deleteAllByExpenseId(anyLong())).thenReturn(null);
        // when
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);
        expenseService.delete(9L);

        // Then
        verify(expenseRepository, never()).deleteById(anyLong());
    }

    @Test
    public void shouldFindByIDSuccess() {
        // Given
        Expense expense = DataGenerator.generateExpenses(1).get(0);
        when(expenseRepository.findById(5L)).thenReturn(Optional.of(expense));

        // when
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);
        ExpenseDTO found = expenseService.findDTOByID(5L);

        Assertions.assertEquals(found, ExpenseConverter.toDTO(expense));
    }

    @Test
    public void shouldFilterExpenseByPlaceIDSuccess() {
        // Given
        List<Expense> expenses = DataGenerator.generateExpenses(6);
        when(expenseRepository.findByUserIdAndPlaceId(6L, 9L)).thenReturn(expenses);

        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        List<ExpenseDTO> found = expenseService.findByPlaceID(6L, 9L);

        // Then
        Assertions.assertEquals(found.size(), expenses.size());

    }


    @Test
    public void shouldReturnEmptyListIfNoExpensesFoundForGivenPlaceSuccess() {
        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        List<ExpenseDTO> found = expenseService.findByPlaceID(6L, 9L);

        // Then
        Assertions.assertTrue(CollectionUtils.isEmpty(found));

    }

    @Test
    public void shouldGetTop5ForGivenUserSuccess() {
        // Given
        List<Expense> expenses = DataGenerator.generateExpenses(6);
        when(expenseRepository.findTop5ByUserIdOrderByDateDesc(6L)).thenReturn(expenses);

        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        List<ExpenseDTO> found = expenseService.findTop5ByUser(6L);

        // Then
        Assertions.assertEquals(found.size(), expenses.size());
    }

    @Test
    public void shouldReturnEmptyIfNoExpensesFoundForGivenUserSuccess() {
        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        List<ExpenseDTO> found = expenseService.findTop5ByUser(6L);

        // Then
        Assertions.assertTrue(CollectionUtils.isEmpty(found));
    }

    @Test
    public void shouldFindExpensesByUserSuccess() {
        // Given
        List<Expense> expenses = DataGenerator.generateExpenses(6);
        when(expenseRepository.findByUserIdOrderByDateDesc(6L)).thenReturn(expenses);

        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        List<ExpenseDTO> found = expenseService.find(6L, 1, null);

        // Then
        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(found.size(), expenses.size());
        verify(expenseRepository, never()).findByUserIdOrderByDateDesc(anyLong(), any());
    }

    @Test
    public void shouldFindExpensesByUserFilteredByPageSuccess() {
        // Given
        List<Expense> expenses = DataGenerator.generateExpenses(6);
        when(expenseRepository.findByUserIdOrderByDateDesc(any(), any())).thenReturn(expenses);

        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        List<ExpenseDTO> found = expenseService.find(6L, 1, 10);

        // Then
        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(found.size(), expenses.size());
        verify(expenseRepository, never()).findByUserIdOrderByDateDesc(anyLong());
    }

    // test empty case

    @Test
    public void shouldReturnEmptyListIFNoResultWhenFindExpensesByUserSuccess() {
        // Given
        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        List<ExpenseDTO> found = expenseService.find(6L, null, null);

        // Then
        Assertions.assertTrue(found.isEmpty());
        verify(expenseRepository, never()).findByUserIdOrderByDateDesc(anyLong(), any());
    }

    @Test
    public void shouldReturnEmptyListIFNoResultWhenFindExpensesByUserFilteredByPageSuccess() {
        // When
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        List<ExpenseDTO> found = expenseService.find(6L, 1, 10);

        // Then
        Assertions.assertTrue(found.isEmpty());
    }

    @Test
    public void shouldUpdateExpenseBillSuccess() {
        // Given
        Expense expense = DataGenerator.generateExpenses(1).get(0);
        expense.setId(5L);
        when(expenseRepository.getById(5L)).thenReturn(expense);
        when(expenseRepository.save(any())).then((Answer<Expense>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (Expense) args[0];
        });

        // WHen
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        ExpenseDTO dto = expenseService.updateExpenseBill(5L, "test");

        // Then
        Assertions.assertEquals(dto.getBill(), "test");
        Assertions.assertEquals(dto.getId(), 5L);
    }

    @Test
    public void shouldSaveExpenseSuccess() {
        // Given
        Expense expense = DataGenerator.generateExpenses(1).get(0);
        expense.setExpenseLines(DataGenerator.generateExpenseLines(6));
        ExpenseDTO toSave = ExpenseConverter.toDTO(expense);
        when(expenseRepository.save(any())).then((Answer<Expense>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((Expense) args[0]).setId(1L);
            return (Expense) args[0];
        });

//        when(expenseLineRepository.save(any())).then((Answer<ExpenseLine>) invocationOnMock -> {
//            Object[] args = invocationOnMock.getArguments();
//            ((Expense) args[0]).setId(new Random().nextLong());
//            return (ExpenseLine) args[0];
//        });

        when(productRepository.save(any())).then((Answer<Product>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((Product) args[0]).setId(new Random().nextLong());
            return (Product) args[0];
        });

        // when
        ExpenseService expenseService = new ExpenseService(expenseRepository, expenseLineRepository,
                placeRepository, addressRepository, productRepository);

        Expense saved = expenseService.save(toSave);

        // Then
        Assertions.assertEquals(expense.getName(), saved.getName());
        Assertions.assertEquals(saved.getId(), 1L);
        verify(productRepository, times(6)).save(any());
    }
}
