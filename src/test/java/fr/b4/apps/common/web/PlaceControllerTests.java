package fr.b4.apps.common.web;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.entities.Address;
import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.common.services.PlaceService;
import fr.b4.apps.expenses.dto.ExpensePlaceDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlaceControllerTests {

    @Mock
    PlaceService placeService;

    @Mock
    RestTemplate restTemplate;

    @Test
    public void shouldFilterGovAddressSuccess()  {
        // Given
        Address address = DataGenerator.generateOneAddress();
        String url = "https://api-adresse.data.gouv.fr/search/?q=" + URLEncoder.encode("ok", StandardCharsets.UTF_8);
        when(restTemplate.getForEntity(url, Object.class)).thenReturn(ResponseEntity.status(200).body(address));

        // When
        PlaceController placeController = new PlaceController(placeService, restTemplate);
        Address found = (Address) placeController.filterAddresses("ok");

        // then
        Assertions.assertEquals(found, address);

    }

    @Test
    public void shouldFilterRestaurantSuccess() {
        // Given
        List<Place> places = DataGenerator.generatePlaces(PlaceType.RESTAURANT, 5);
        when(placeService.filter("franprix", PlaceType.RESTAURANT)).thenReturn(places);

        // When
        PlaceController placeController = new PlaceController(placeService, restTemplate);
        List<Place> found = placeController.filterRestaurants("franprix");

        // Then
        verify(placeService, times(1)).filter("franprix", PlaceType.RESTAURANT);
        Assertions.assertEquals(found, places);

    }

    @Test
    public void shouldFilterStoreSuccess() {
        // Given
        List<Place> places = DataGenerator.generatePlaces(PlaceType.STORE, 5);
        when(placeService.filter("franprix", PlaceType.STORE)).thenReturn(places);

        // When
        PlaceController placeController = new PlaceController(placeService, restTemplate);
        List<Place> found = placeController.filterStores("franprix");

        // Then
        verify(placeService, times(1)).filter("franprix", PlaceType.STORE);
        Assertions.assertEquals(found, places);
    }


    @Test
    public void shouldGetStoreRankingSuccess() {
        // Given
        List<ExpensePlaceDTO> stats = DataGenerator.generateExpensePlaces(PlaceType.STORE, 6);
        when(placeService.getPlaceRanking(PlaceType.STORE.toString())).thenReturn(stats);

        // When
        PlaceController placeController = new PlaceController(placeService, restTemplate);
        List<ExpensePlaceDTO> found = placeController.getStoreRanking();

        // Then
        verify(placeService, times(1)).getPlaceRanking(PlaceType.STORE.toString());
        Assertions.assertEquals(found, stats);
    }

    @Test
    public void shouldGetRestaurantRankingSuccess() {
        // Given
        List<ExpensePlaceDTO> stats = DataGenerator.generateExpensePlaces(PlaceType.RESTAURANT, 6);
        when(placeService.getPlaceRanking(PlaceType.RESTAURANT.toString())).thenReturn(stats);

        // When
        PlaceController placeController = new PlaceController(placeService, restTemplate);
        List<ExpensePlaceDTO> found = placeController.getRestaurantRanking();

        // Then
        verify(placeService, times(1)).getPlaceRanking(PlaceType.RESTAURANT.toString());
        Assertions.assertEquals(stats, found);

    }


}
