package fr.b4.apps.expenses.repositories;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.common.entities.Place;
import fr.b4.apps.expenses.entities.Expense;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    /**
     * @return select sum(el.price) from expense join expense_line el on expense.id = el.expense_id where expense.date >= '2021-09-01'
     */
    @Query(value = "select sum(el.price) from expense join expense_line el on expense.id = el.expense_id where expense.user_id=:userID and expense.date>=:targetDate", nativeQuery = true)
    public Float sumAllExpenses(@Param("userID") long userID, @Param("targetDate") LocalDate targetDate);

    @Query(value = "select count(*) from expense join expense_line el on expense.id = el.expense_id where expense.user_id=:userID and expense.date>=:targetDate", nativeQuery = true)
    public Integer countAllExpenses(@Param("userID") long userID, @Param("targetDate") LocalDate targetDate);

    @Query(value = "select sum(price) from expense  inner join expense_line on expense.id=expense_line.expense_id inner join place on expense.place_id=place.id" +
            " where expense.user_id=:userID and expense.date >=:targetDate and place.type=:type", nativeQuery = true)
    Float sumAllExpenses(@Param("userID") Long id, @Param("targetDate") LocalDate targetDate, @Param("type") String type);

    @Query(value = "select count(*) from expense inner join place on expense.place_id=place.id" +
            " where expense.user_id=:userID and expense.date >=:targetDate  and place.type=:type", nativeQuery = true)
    Integer countAllExpenses(@Param("userID") Long id, @Param("targetDate") LocalDate targetDate, @Param("type") String type);

    public List<Expense> findByUserIdOrderByDateDesc(Long userID, Pageable pageable);

    public List<Expense> findByUserIdOrderByDateDesc(Long userID);

    public List<Expense> findTop5ByUserIdOrderByDateDesc(Long userID);

    public List<Expense> findByUserIdAndPlaceId(Long userID, Long placeID);
}
