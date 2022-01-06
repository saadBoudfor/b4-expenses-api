package fr.b4.apps.expenses.process;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.expenses.dto.ExpenseBasicStatsDTO;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.services.BudgetService;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseProcessTests {
    @Mock
    ExpenseService expenseService;

    @Mock
    BudgetService budgetService;


    @Test
    public void shouldGetBasicStatsSuccess() {
        // Given
        ExpenseBasicStatsDTO dto = DataGenerator.generateBasicStats();
        when(expenseService.getExpenseStats(3L)).thenReturn(dto);
        when(budgetService.getTarget(3L)).thenReturn(45.3f);

        // When
        ExpenseProcess expenseProcess = new ExpenseProcess(budgetService, expenseService);
        ExpenseBasicStatsDTO found = expenseProcess.getBasicStats(3L);

        // Then
        Assertions.assertEquals(found, dto);
        verify(expenseService, times(1)).getExpenseStats(anyLong());
        verify(budgetService, times(1)).getTarget(anyLong());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfGivenIDNull() {
        // Given
        ExpenseProcess expenseProcess = new ExpenseProcess(budgetService, expenseService);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> expenseProcess.addBill(3L, null));
    }


    @Test
    public void shouldThrowIllegalArgumentExceptionIfFileNull() throws IOException {
        // Given
        File file = new File("test.txt");
        file.createNewFile();
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", input.readAllBytes());

        // When
        ExpenseProcess expenseProcess = new ExpenseProcess(budgetService, expenseService);

        // Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> expenseProcess.addBill(0L, multipartFile));
        Assertions.assertThrows(IllegalArgumentException.class, () -> expenseProcess.addBill(-9L, multipartFile));
    }

    @Test
    public void shouldAddBillSuccess() throws IOException {
        // Given
        ExpenseDTO dto = ExpenseConverter.toDTO(DataGenerator.generateExpenses(1).get(0));
        when(expenseService.updateExpenseBill(any(), any())).thenReturn(dto);

        File file = new File("test.txt");
        file.createNewFile();
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", input.readAllBytes());

        // When
        ExpenseProcess expenseProcess = new ExpenseProcess(budgetService, expenseService);
        ExpenseDTO saved = expenseProcess.addBill(6L, multipartFile);

        // Then
        Assertions.assertEquals(dto, saved);
        verify(expenseService, times(1)).updateExpenseBill(anyLong(), any());
    }

    @Test
    public void shouldSaveExpenseSuccess() {
        // Given
        when(expenseService.save(any())).then((Answer<Expense>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return ExpenseConverter.toExpense((ExpenseDTO) args[0]);
        });
        ExpenseDTO toSave = ExpenseConverter.toDTO(DataGenerator.generateExpenses(1).get(0));
        // When
        ExpenseProcess expenseProcess = new ExpenseProcess(budgetService, expenseService);
        ExpenseDTO saved = expenseProcess.save(toSave, null);

        // Then
        Assertions.assertEquals(saved, toSave);
    }


    @Test
    public void shouldSaveExpenseWithBillSuccess() throws IOException {
        // Given
        when(expenseService.save(any())).then((Answer<Expense>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return ExpenseConverter.toExpense((ExpenseDTO) args[0]);
        });

        File file = new File("test.txt");
        file.createNewFile();
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", input.readAllBytes());


        ExpenseDTO toSave = ExpenseConverter.toDTO(DataGenerator.generateExpenses(1).get(0));
        // When
        ExpenseProcess expenseProcess = new ExpenseProcess(budgetService, expenseService);
        ExpenseDTO saved = expenseProcess.save(toSave, multipartFile);

        // Then
        Assertions.assertEquals(saved, toSave);
        Assertions.assertEquals("test.txt", saved.getBill());
    }


}
