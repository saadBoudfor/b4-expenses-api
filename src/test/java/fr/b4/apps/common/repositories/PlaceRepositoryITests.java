package fr.b4.apps.common.repositories;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.expenses.dto.ExpensePlaceDTO;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.entities.ExpenseLine;
import fr.b4.apps.expenses.repositories.ExpenseLineRepository;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class PlaceRepositoryITests {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseLineRepository expenseLineRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldFilterPlacesByName() {
        //given:
        Place place = DataGenerator.generateOnePlace(PlaceType.RESTAURANT);
        place.setName("Carrefour 4eme");
        addressRepository.save(place.getAddress());
        placeRepository.save(place);

        //when:
        List<Place> founds = placeRepository.findByNameContains("Carre");

        //then:
        Assertions.assertFalse(founds.isEmpty());
        Assertions.assertEquals(place, founds.get(0));
    }

    @Test
    public void shouldReturnNullIfNoPlaceFound() {
        //given:
        Place place = DataGenerator.generateOnePlace(PlaceType.RESTAURANT);
        place.setName("Casino 4eme");
        addressRepository.save(place.getAddress());
        placeRepository.save(place);

        //when:
        List<Place> founds = placeRepository.findByNameContains("Carr");

        //then:
        Assertions.assertTrue(founds.isEmpty());
    }

    @Test
    public void shouldGetRestaurantRankingSuccess() {
        // Given 5 expenses for place1 and 3 expenses for place2:
        Place place1 = DataGenerator.generateOnePlace(PlaceType.RESTAURANT);
        Place place2 = DataGenerator.generateOnePlace(PlaceType.RESTAURANT);
        Place place3 = DataGenerator.generateOnePlace(PlaceType.STORE);

        List<Expense> expensesRestaurant1 = DataGenerator.generateExpensesForPlace(place1, 3);
        List<Expense> expensesRestaurant2 = DataGenerator.generateExpensesForPlace(place2, 9);
        List<Expense> expensesStore3 = DataGenerator.generateExpensesForPlace(place2, 6);

        savePlaceInDB(place1);
        savePlaceInDB(place2);
        savePlaceInDB(place3);

        saveExpensesInDB(expensesRestaurant1);
        saveExpensesInDB(expensesRestaurant2);
        saveExpensesInDB(expensesStore3);

        // When get place ranking:
        List<Object[]> rawRanking = placeRepository.getPlacesRanking(PlaceType.RESTAURANT.toString());

        //then
        Assertions.assertFalse(rawRanking.isEmpty());
        List<ExpensePlaceDTO> ranking = rawRanking.stream()
                .map(ExpenseConverter::convertToExpensePlaceRanking)
                .collect(Collectors.toList());
        Assertions.assertEquals(ranking.get(0).getPlace().getId(), place2.getId()); //have 9 expenses
        Assertions.assertEquals(ranking.get(1).getPlace().getId(), place1.getId()); // have 3 expenses
        Assertions.assertEquals(ranking.size(), 2);
    }

    private void saveExpensesInDB(List<Expense> expenses) {
        expenseRepository.saveAll(expenses);
        expenses.forEach(expense -> {
            productRepository.saveAll(expense
                    .getExpenseLines()
                    .stream()
                    .map(ExpenseLine::getProduct)
                    .collect(Collectors.toList()));
            expense.getExpenseLines().forEach(expenseLine -> expenseLine.setExpense(expense));
            expenseLineRepository.saveAll(expense.getExpenseLines());
        });
    }

    private void savePlaceInDB(Place place) {
        addressRepository.save(place.getAddress());
        placeRepository.save(place);
    }

}
