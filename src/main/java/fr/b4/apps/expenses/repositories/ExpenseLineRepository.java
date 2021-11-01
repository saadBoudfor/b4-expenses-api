package fr.b4.apps.expenses.repositories;

import fr.b4.apps.expenses.entities.ExpenseLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseLineRepository extends JpaRepository<ExpenseLine, Long> {
    public void deleteAllByExpenseId(Long id);
}
