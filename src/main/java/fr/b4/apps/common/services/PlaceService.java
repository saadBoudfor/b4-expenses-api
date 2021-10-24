package fr.b4.apps.common.services;

import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.common.repositories.PlaceRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> filter(String name, PlaceType type) {
        return placeRepository
                .findByNameContains(name)
                .stream().filter(place -> place.getType().equals(type))
                .collect(Collectors.toList());
    }
}
