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
    public Float getAmountExpense(@Param("userID") long userID, @Param("targetDate") LocalDate targetDate);

    public List<Expense> findByUserOrderByDateDesc(User user, Pageable pageable);

    public List<Expense> findByUserOrderByDateDesc(User user);

    public List<Expense> findTop5ByUserOrderByDateDesc(User user);

    public List<Expense> findByUserAndPlace(User user, Place place);

    @Query(value = "select count(*) from expense inner join place on expense.place_id=place.id where place.type=:type and expense.user_id=:userID", nativeQuery = true)
    public Float countExpenses(@Param("userID") long userID, @Param("type") String type);

    @Query(value = "select sum(price) from expense inner join place on expense.place_id=place.id inner join expense_line on expense.id=expense_line.expense_id  where place.type=:type and expense.user_id=:userID", nativeQuery = true)
    public Float sumExpenses(@Param("userID") long userID, @Param("type") String type);

    @Query(value = "select sum(price) from expense  inner join expense_line on expense.id=expense_line.expense_id where expense.user_id=:userID and expense.date >=:firstDayOfCurrentWeek", nativeQuery = true)
    Float getCurrentWeekTotal(@Param("userID") Long id, @Param("firstDayOfCurrentWeek") LocalDate firstDayOfCurrentWeek);

    @Query(value = "select count(*) from expense where expense.user_id=:userID and expense.date >=:firstDayOfCurrentWeek", nativeQuery = true)
    Integer getCurrentWeekCount(@Param("userID") Long id, @Param("firstDayOfCurrentWeek") LocalDate firstDayOfCurrentWeek);


    @Query(value = "select sum(price) from expense  inner join expense_line on expense.id=expense_line.expense_id inner join place on expense.place_id=place.id" +
            " where expense.user_id=:userID and expense.date >=:firstDayOfCurrentWeek and place.type=:type", nativeQuery = true)
    Float getCurrentWeekTotalByPlaceType(@Param("userID") Long id, @Param("firstDayOfCurrentWeek") LocalDate firstDayOfCurrentWeekn, @Param("type") String type);

    @Query(value = "select count(*) from expense inner join place on expense.place_id=place.id" +
            " where expense.user_id=:userID and expense.date >=:firstDayOfCurrentWeek  and place.type=:type", nativeQuery = true)
    Integer getCurrentWeekCountByPlaceType(@Param("userID") Long id, @Param("firstDayOfCurrentWeek") LocalDate firstDayOfCurrentWeek, @Param("type") String type);
}
