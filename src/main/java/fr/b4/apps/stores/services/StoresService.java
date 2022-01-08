package fr.b4.apps.stores.services;

import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.stores.dto.StoreDTO;
import fr.b4.apps.stores.entities.Store;
import fr.b4.apps.stores.repositories.StoreRepository;
import fr.b4.apps.stores.util.converters.StoreConverters;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StoresService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoresService(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    /**
     * add new store
     *
     * @return saved store
     */
    public StoreDTO save(@NonNull StoreDTO storeDTO) throws BadRequestException {
        if (!ObjectUtils.isEmpty(storeDTO.getId())) {
            log.error("new store should not have a defined id");
            throw new BadRequestException("new store should not have a defined id");
        }

        if (StringUtils.isEmpty(storeDTO.getName())) {
            log.error("store's name missing");
            throw new BadRequestException("store's name missing");
        }

        if (ObjectUtils.isEmpty(storeDTO.getOwner()) || ObjectUtils.isEmpty(storeDTO.getOwner().getId())) {
            log.error("store's owner missing");
            throw new BadRequestException("store's owner missing");
        }

        if (!userRepository.existsById(storeDTO.getOwner().getId())) {
            log.error("store's owner is unknown");
            throw new BadRequestException("store's owner is unknown");
        }

        Store store = StoreConverters.toStore(storeDTO);
        store = storeRepository.save(store);
        return StoreConverters.toDTO(store);
    }

    public StoreDTO update(@NonNull StoreDTO storeDTO) {
        if (ObjectUtils.isEmpty(storeDTO.getId())) {
            log.error("failed to update store. store's id missing");
            throw new BadRequestException("failed to update store. store's id missing");
        }
        Store saved = storeRepository.getById(storeDTO.getId());
        if (ObjectUtils.isEmpty(saved)) {
            log.error("failed to update store. Store {} unknown", storeDTO);
            throw new ResourceNotFoundException("failed to update store. Store " + storeDTO.getId() + " unknown");
        }
        saved.setName(storeDTO.getName());
        saved.setPlanUrl(storeDTO.getPlanUrl());
        saved = storeRepository.save(saved);
        return StoreConverters.toDTO(saved);
    }

    public List<StoreDTO> getByUserID(@NonNull Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("store's owner unknown");
            throw new BadRequestException("store's owner unknown");
        }

        List<Store> stores = storeRepository.getByOwnerId(userId);
        if (ObjectUtils.isEmpty(stores)) {
            return new ArrayList<>();
        }
        return stores.stream().map(StoreConverters::toDTO).collect(Collectors.toList());
    }

    public StoreDTO getByID(@NonNull Long id) {
        Store found = storeRepository.getById(id);
        if (ObjectUtils.isEmpty(found)) {
            log.error("Store {} unknown", id);
            throw new ResourceNotFoundException("cannot find store with id: " + id);
        }
        return StoreConverters.toDTO(found);
    }

    public void delete(@NonNull long id) {
        if (!storeRepository.existsById(id)) {
            log.error("failed to delete store {} store's id unknown", id);
            throw new ResourceNotFoundException("store's id unknown");
        }
        log.warn("delete store {}", id);
        storeRepository.deleteById(id);
    }
}
