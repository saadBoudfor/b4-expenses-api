package fr.b4.apps.common.web;

import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.common.services.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("addresses/{search}")
    public Object filterAddresses(@PathVariable(value = "search", required = false) String search) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response
                = restTemplate.getForEntity("https://api-adresse.data.gouv.fr/search/?q=" + URLEncoder.encode(search, StandardCharsets.UTF_8), Object.class);
        return response.getBody();
    }

    @GetMapping("restaurants/{search}")
    public List<Place> filterRestaurants(@PathVariable(value = "search", required = true) String search) {
        return placeService.filter(search, PlaceType.RESTAURANT);
    }

    @GetMapping("stores/{search}")
    public List<Place> filterStores(@PathVariable(value = "search", required = true) String search) {
        return placeService.filter(search, PlaceType.STORE);
    }

}
