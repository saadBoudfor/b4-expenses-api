package fr.b4.apps.common.repositories;

import fr.b4.apps.common.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    /**
     * Filter place by name
     *
     * @param search searched place name
     * @return places with  given name
     */
    List<Place> findByNameContains(String search);


    /**
     * Get Top 5 Places ranking
     *
     * @param place_type restaurant/store
     * @return TOP 5 places where the user spent the most money.
     * Get the total and count of expenses for each place in ranking
     */
    @Query(value = "select place_id, sum(total), count(expense_id) as total " +
            " from (select expense.id as expense_id, date , place_id, sum(price) as total " +
            " from expense inner join expense_line on expense_line.expense_id = expense.id " +
            " inner join place on place.id=place_id " +
            " where place_id is not null and type=:place_type group by expense.id, date, place_id) as T " +
            " group by T.place_id" +
            " order by total desc limit 5", nativeQuery = true)
    List<Object[]> getPlacesRanking(@Param("place_type") String place_type);
}
