package fr.b4.apps.common.services;

import fr.b4.apps.common.entities.Place;
import fr.b4.apps.common.entities.PlaceType;
import fr.b4.apps.common.repositories.PlaceRepository;
import fr.b4.apps.expenses.dto.ExpensePlaceDTO;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import org.springframework.stereotype.Component;

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

    public List<ExpensePlaceDTO> getPlaceRanking(String placeType) {
        List<Object[]> rankingRaw = placeRepository.getPlacesRanking(placeType);
        return rankingRaw.stream()
                .map(ExpenseConverter::convertToExpensePlaceRanking)
                .map(this::updatePlace) // return Place object instead of Place name
                .collect(Collectors.toList());
    }

    public Place get(Long id) {
        return placeRepository.findById(id).orElse(null);
    }

    public ExpensePlaceDTO updatePlace(ExpensePlaceDTO expensePlaceDTO) {
        Long id = expensePlaceDTO.getPlace().getId();
        placeRepository.findById(id).ifPresent(expensePlaceDTO::setPlace);
        return expensePlaceDTO;
    }
}
