package fr.b4.apps.expenses.repositories;

import fr.b4.apps.expenses.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
