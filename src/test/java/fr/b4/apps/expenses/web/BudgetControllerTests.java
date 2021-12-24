package fr.b4.apps.expenses.web;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.expenses.entities.Budget;
import fr.b4.apps.expenses.services.BudgetService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BudgetControllerTests {
    @Mock
    UserRepository userRepository;

    @Mock
    BudgetService budgetService;

    @Test
    public void shouldSaveBudgetSuccess() {
        // Given
        Optional<User> authenticated = Optional.of(DataGenerator.generateUser());
        authenticated.get().setId(5L);
        Budget budget = DataGenerator.generateBudget();
        budget.setUser(authenticated.get());
        when(userRepository.findById(5L)).thenReturn(authenticated);
        when(budgetService.defineCurrentMonthBudget(budget.getTarget(), budget.getUser())).thenReturn(budget);

        // when
        BudgetController budgetController = new BudgetController(userRepository, budgetService);
        Budget saved = budgetController.saveBudget("5", budget);

        // Then
        Assertions.assertEquals(saved, budget);
    }

    @Test
    public void shouldGetAllBudgetSuccess() {
        // Given
        List<Budget> budgets = DataGenerator.generateBudgets(6);
        when(budgetService.getByUserID(5L)).thenReturn(budgets);

        // when
        BudgetController budgetController = new BudgetController(userRepository, budgetService);
        List<Budget> found = budgetController.getAll("5");

        // Then
        Assertions.assertEquals(found, budgets);
    }

    @Test
    public void shouldGetOneBudgetSuccess() {
        // Given
        Budget budget = DataGenerator.generateBudget();
        when(budgetService.getCurrentByUserID(5L)).thenReturn(budget);

        // when
        BudgetController budgetController = new BudgetController(userRepository, budgetService);
        Budget found = budgetController.get("5");

        // Then
        Assertions.assertEquals(found, budget);
    }
}
