package fr.b4.apps.expenses.repositories;

import fr.b4.apps.expenses.dto.NutrientStatDTO;
import fr.b4.apps.expenses.entities.ExpenseLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseLineRepository extends JpaRepository<ExpenseLine, Long> {
    public void deleteAllByExpenseId(Long id);
    @Query(value = "select score, count(score)  from (select nutrient_levels.*, score from expense_line join product on product.id=expense_line.product_id join nutrient_levels on  product.nutrient_levels_id=nutrient_levels.id) as nutrient_stats group by score", nativeQuery = true)
    public List<Object[]> getNutrientStats();
}
