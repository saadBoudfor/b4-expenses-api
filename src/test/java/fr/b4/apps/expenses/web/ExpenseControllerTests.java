package fr.b4.apps.expenses.web;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.process.ExpenseProcess;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseControllerTests {
    @Mock
    ExpenseProcess expenseProcess;

    @Mock
    ExpenseService expenseService;

    @Test
    public void shouldGeBasicStatsSuccess() {
        // Given
        ExpenseBasicStatsDTO expenseBasicStatsDTO = DataGenerator.generateBasicStats();
        when(expenseProcess.getBasicStats(5L)).thenReturn(expenseBasicStatsDTO);

        // When
        ExpenseController expenseController = new ExpenseController(expenseProcess, expenseService);
        ExpenseBasicStatsDTO res = expenseController.getBasicStats("5");

        // Then
        Assertions.assertEquals(res, expenseBasicStatsDTO);
        verify(expenseProcess, times(1)).getBasicStats(any());
    }

    @Test
    public void shouldGetRestaurantStatsSuccess() {
        // Given
        ExpenseBasicStatsDTO expenseBasicStatsDTO = DataGenerator.generateBasicStats();
        when(expenseService.getExpenseStats(5L, PlaceType.RESTAURANT)).thenReturn(expenseBasicStatsDTO);

        // When
        ExpenseController expenseController = new ExpenseController(expenseProcess, expenseService);
        ExpenseBasicStatsDTO res = expenseController.getRestaurantBasicStats("5");

        // Then
        Assertions.assertEquals(res, expenseBasicStatsDTO);
        verify(expenseService, times(1)).getExpenseStats(any(), any());
    }

    @Test
    public void shouldGetNutrientStatSuccess() {
        // Given
        ExpenseBasicStatsDTO expenseBasicStatsDTO = DataGenerator.generateBasicStats();
        when(expenseService.getExpenseStats(5L, PlaceType.STORE)).thenReturn(expenseBasicStatsDTO);

        // When
        ExpenseController expenseController = new ExpenseController(expenseProcess, expenseService);
        ExpenseBasicStatsDTO res = expenseController.getStoresBasicStats("5");

        // Then
        Assertions.assertEquals(res, expenseBasicStatsDTO);
        verify(expenseService, times(1)).getExpenseStats(any(), any());
    }

    @Test
    public void shouldGetAllSuccess() {
        // Given
        List<ExpenseDTO> expenseDTOList = ExpenseConverter.toDTO(DataGenerator.generateExpenses(10));
        when(expenseService.find(5L, 1, 1)).thenReturn(expenseDTOList);

        // When
        ExpenseController expenseController = new ExpenseController(expenseProcess, expenseService);
        List<ExpenseDTO> res = expenseController.getAll("5", 1, 1);


        // Then
        Assertions.assertEquals(res, expenseDTOList);
        verify(expenseService, times(1)).find(any(), any(), any());
    }

    @Test
    public void shouldGetExpenseByPlaceSuccess() {
        // Given
        List<ExpenseDTO> expenseDTOList = ExpenseConverter.toDTO(DataGenerator.generateExpenses(10));
        when(expenseService.findTop5ByUser(5L)).thenReturn(expenseDTOList);

        // When
        ExpenseController expenseController = new ExpenseController(expenseProcess, expenseService);
        List<ExpenseDTO> res = expenseController.findTop5ByUser("5");


        // Then
        Assertions.assertEquals(res, expenseDTOList);
        verify(expenseService, times(1)).findTop5ByUser(any());
    }

    @Test
    public void shouldGetExpenseDTOById() {
        // Given
        ExpenseDTO expenseDTO = ExpenseConverter.toDTO(DataGenerator.generateExpenses(1).get(0));
        when(expenseService.findDTOByID(5L)).thenReturn(expenseDTO);

        // When
        ExpenseController expenseController = new ExpenseController(expenseProcess, expenseService);
        ExpenseDTO res = expenseController.getByID("5");

        // Then
        Assertions.assertEquals(res, expenseDTO);
        verify(expenseService, times(1)).findDTOByID(any());
    }

    @Test
    public void shouldSaveExpenseSuccess() throws IOException, BadRequestException {
        // Given
        File file = new File("test.txt");
        file.createNewFile();
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", input.readAllBytes());

        ExpenseDTO expenseDTO = ExpenseConverter.toDTO(DataGenerator.generateExpenses(1).get(0));
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(4L);
        when(expenseProcess.save(dto, multipartFile)).thenReturn(expenseDTO);

        // When
        ExpenseController expenseController = new ExpenseController(expenseProcess, expenseService);
        ExpenseDTO saved = expenseController.save(multipartFile, "{\"id\": 4}");

        // Then
        Assertions.assertEquals(saved, expenseDTO);
    }

    @Test
    public void shouldDeleteExpenseSuccess() {
        // Given
        doNothing().when(expenseService).delete(any());

        // When
        ExpenseController expenseController = new ExpenseController(expenseProcess, expenseService);
        expenseController.delete(5L);

        // Then
        verify(expenseService, times(1)).delete(5L);
    }

    @Test
    public void shouldAddBillSuccess() throws IOException, BadRequestException {
        // Given
        File file = new File("test.txt");
        file.createNewFile();
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", input.readAllBytes());

        ExpenseDTO expenseDTO = ExpenseConverter.toDTO(DataGenerator.generateExpenses(1).get(0));
        when(expenseProcess.addBill(5L, multipartFile)).thenReturn(expenseDTO);

        // When
        ExpenseController expenseController = new ExpenseController(expenseProcess, expenseService);
        ExpenseDTO saved = expenseController.addBill(5L, multipartFile);

        // Then
        Assertions.assertEquals(saved, expenseDTO);
    }
}
