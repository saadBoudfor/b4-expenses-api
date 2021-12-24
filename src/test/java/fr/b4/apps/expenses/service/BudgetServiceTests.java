package fr.b4.apps.expenses.service;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.clients.entities.User;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.expenses.entities.Budget;
import fr.b4.apps.expenses.repositories.BudgetRepository;
import fr.b4.apps.expenses.services.BudgetService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BudgetServiceTests {
    @Mock
    BudgetRepository budgetRepositoryMock;

    @Test
    public void shouldGetCurrentBudgetByUsersSuccess() {
        // given
        List<Budget> budgets = DataGenerator.generateBudgets(8);
        when(budgetRepositoryMock.getByUserId(4L)).thenReturn(budgets);

        // when
        BudgetService budgetService = new BudgetService(budgetRepositoryMock);
        List<Budget> found = budgetService.getByUserID(4L);

        // then
        Assertions.assertEquals(budgets, found);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForInvalidUserID() {
        // when
        BudgetService budgetService = new BudgetService(budgetRepositoryMock);

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> budgetService.getByUserID(0L));

        Assertions.assertThrows(IllegalArgumentException.class, () -> budgetService.getByUserID(-99L));

        Assertions.assertThrows(IllegalArgumentException.class, () -> budgetService.getByUserID(null));
    }

    @Test
    public void shouldGetCurrentBudgetForGivenUserSuccess() {
        // given
        List<Budget> budgets = DataGenerator.generateBudgets(8);
        when(budgetRepositoryMock.getByUserId(4L)).thenReturn(budgets);

        // when
        BudgetService budgetService = new BudgetService(budgetRepositoryMock);
        Budget found = budgetService.getCurrentByUserID(4L);

        // then
        Assertions.assertEquals(budgets.get(0), found);
        verify(budgetRepositoryMock, times(1)).getByUserId(anyLong());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionIFCurrentBudgetUnknown() {
        // given
        when(budgetRepositoryMock.getByUserId(4L)).thenReturn(null);


        // when
        BudgetService budgetService = new BudgetService(budgetRepositoryMock);

        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> budgetService.getCurrentByUserID(4L));
    }

    @Test
    public void shouldReturnCurrentMonthTargetForGivenUserSuccess() {
        // given
        List<Budget> budgets = DataGenerator.generateBudgets(8);
        when(budgetRepositoryMock.getBudgetByDateAndUserId(any(), anyLong())).thenReturn(budgets);

        // when
        BudgetService budgetService = new BudgetService(budgetRepositoryMock);
        Float target = budgetService.getTarget(5L);

        //Then
        verify(budgetRepositoryMock, times(1)).getBudgetByDateAndUserId(any(), anyLong());
        Assertions.assertEquals(target, budgets.get(0).getTarget());
    }

    @Test
    public void shouldReturnZeroIfBudgetUnknown() {
        // Given
        when(budgetRepositoryMock.getBudgetByDateAndUserId(any(), anyLong())).thenReturn(null);

        // when
        BudgetService budgetService = new BudgetService(budgetRepositoryMock);
        Float target = budgetService.getTarget(5L);

        //Then
        verify(budgetRepositoryMock, times(1)).getBudgetByDateAndUserId(any(), anyLong());
        Assertions.assertEquals(target, 0.0f);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForInvalidUserId() {
        // when
        BudgetService budgetService = new BudgetService(budgetRepositoryMock);

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> budgetService.getTarget(0L));

        Assertions.assertThrows(IllegalArgumentException.class, () -> budgetService.getTarget(-99L));

        Assertions.assertThrows(IllegalArgumentException.class, () -> budgetService.getTarget(null));
    }

    @Test
    public void shouldSaveBudgetSuccess() {
        // Given
        when(budgetRepositoryMock.save(any())).then((Answer<Budget>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (Budget) args[0];
        });

        // when
        BudgetService budgetService = new BudgetService(budgetRepositoryMock);
        Budget saved = budgetService.defineCurrentMonthBudget(5.6f, DataGenerator.generateUser());

        // Then
        Assertions.assertEquals(saved.getTarget(), 5.6f);
    }

    @Test
    public void shouldUpdateBudgetSuccess() {
        // Given
        List<Budget> budgets = DataGenerator.generateBudgets(8);
        when(budgetRepositoryMock.getBudgetByDateAndUserId(any(), anyLong())).thenReturn(budgets);
        when(budgetRepositoryMock.save(any())).then((Answer<Budget>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return (Budget) args[0];
        });
        User authenticated = DataGenerator.generateUser();
        authenticated.setId(9L);

        // When
        BudgetService budgetService = new BudgetService(budgetRepositoryMock);
        Budget saved = budgetService.defineCurrentMonthBudget(6.9f, authenticated);

        Assertions.assertEquals(budgets.get(0), saved);
    }

}
