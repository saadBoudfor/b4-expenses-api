package fr.b4.apps.common.services;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.common.repositories.PlaceRepository;
import fr.b4.apps.expenses.dto.ExpensePlaceDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class PlaceServiceTests {
    @Mock
    PlaceRepository placeRepository;

    @Test
    public void shouldFilterByPlaceSuccess() {
        List<Place> places = DataGenerator.generatePlaces(PlaceType.RESTAURANT, 3);
        places.addAll(DataGenerator.generatePlaces(PlaceType.STORE, 5));
        Mockito.when(placeRepository.findByNameContains("test")).thenReturn(places);

        PlaceService placeService = new PlaceService(placeRepository);

        List<Place> found = placeService.filter("test", PlaceType.RESTAURANT);

        Mockito.verify(placeRepository, Mockito.times(1)).findByNameContains("test");
        Assertions.assertEquals(found.size(), 3);
    }

    @Test
    public void shouldGetPlaceRankingSuccess() {
        List<Object[]> placeDTOS = DataGenerator.generateExpensePlaceRawData(6);
        Mockito.when(placeRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(DataGenerator.generateOnePlace(PlaceType.RESTAURANT)));

        Mockito.when(placeRepository.getPlacesRanking(PlaceType.RESTAURANT.toString())).thenReturn(placeDTOS);
        PlaceService placeService = new PlaceService(placeRepository);
        List<ExpensePlaceDTO> ranking = placeService.getPlaceRanking(PlaceType.RESTAURANT.toString());

        Assertions.assertEquals(ranking.size(), 6);
        Mockito.verify(placeRepository, Mockito.times(1))
                .getPlacesRanking(PlaceType.RESTAURANT.toString());
    }


    @Test
    public void shouldUpdatePlaceSuccess() {
        ExpensePlaceDTO expensePlaceDTO = DataGenerator.generateExpensePlace(PlaceType.RESTAURANT);
        expensePlaceDTO.getPlace().setId(5L);
        Mockito.when(placeRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(DataGenerator.generateOnePlace(PlaceType.RESTAURANT)));
        PlaceService placeService = new PlaceService(placeRepository);
        placeService.updatePlace(expensePlaceDTO);

        Mockito.verify(placeRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    public void shouldGetPlaceByIDSuccess() {
        Place place = DataGenerator.generateOnePlace(PlaceType.RESTAURANT);
        place.setId(5L);
        Mockito.when(placeRepository.findById(5L))
                .thenReturn(Optional.of(place));

        PlaceService placeService = new PlaceService(placeRepository);
        Place found = placeService.get(5L);

        Assertions.assertEquals(placeService.get(5L), found);
        Assertions.assertNull(placeService.get(8L));
    }

}
