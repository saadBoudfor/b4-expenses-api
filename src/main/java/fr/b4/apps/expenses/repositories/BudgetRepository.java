package fr.b4.apps.expenses.repositories;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.expenses.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    public List<Budget> getBudgetByDateAndUser(LocalDate date, User user);
    public List<Budget> getByUserId(Long userID);
}
