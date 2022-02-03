package fr.b4.apps.storages.services;

import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.storages.dto.StorageDTO;
import fr.b4.apps.storages.entities.Storage;
import fr.b4.apps.storages.repositories.StoragesRepository;
import fr.b4.apps.storages.util.converters.StoragesConverters;
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
public class StoragesService {
    private final StoragesRepository storagesRepository;
    private final UserRepository userRepository;

    public StoragesService(StoragesRepository storagesRepository, UserRepository userRepository) {
        this.storagesRepository = storagesRepository;
        this.userRepository = userRepository;
    }

    /**
     * add new store
     *
     * @return saved store
     */
    public StorageDTO save(@NonNull StorageDTO storageDTO) throws BadRequestException {
        if (!ObjectUtils.isEmpty(storageDTO.getId())) {
            log.error("new store should not have a defined id");
            throw new BadRequestException("new store should not have a defined id");
        }

        if (StringUtils.isEmpty(storageDTO.getName())) {
            log.error("store's name missing");
            throw new BadRequestException("store's name missing");
        }

        if (ObjectUtils.isEmpty(storageDTO.getOwner()) || ObjectUtils.isEmpty(storageDTO.getOwner().getId())) {
            log.error("store's owner missing");
            throw new BadRequestException("store's owner missing");
        }

        if (!userRepository.existsById(storageDTO.getOwner().getId())) {
            log.error("store's owner is unknown");
            throw new BadRequestException("store's owner is unknown");
        }

        Storage storage = StoragesConverters.toStore(storageDTO);
        storage = storagesRepository.save(storage);
        return StoragesConverters.toDTO(storage);
    }

    public StorageDTO update(@NonNull StorageDTO storageDTO) {
        if (ObjectUtils.isEmpty(storageDTO.getId())) {
            log.error("failed to update store. store's id missing");
            throw new BadRequestException("failed to update store. store's id missing");
        }
        Storage saved = storagesRepository.getById(storageDTO.getId());
        if (ObjectUtils.isEmpty(saved)) {
            log.error("failed to update store. Store {} unknown", storageDTO);
            throw new ResourceNotFoundException("failed to update store. Store " + storageDTO.getId() + " unknown");
        }
        saved.setName(storageDTO.getName());
        saved.setPlanUrl(storageDTO.getPlanUrl());
        saved = storagesRepository.save(saved);
        return StoragesConverters.toDTO(saved);
    }

    public List<StorageDTO> getByUserID(@NonNull Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("store's owner unknown");
            throw new BadRequestException("store's owner unknown");
        }

        List<Storage> storages = storagesRepository.getByOwnerId(userId);
        if (ObjectUtils.isEmpty(storages)) {
            return new ArrayList<>();
        }
        log.debug("load {} storages for user {}", storages, storages.get(0).getOwner().getName());
        return storages.stream().map(StoragesConverters::toDTO).collect(Collectors.toList());
    }

    public StorageDTO getByID(@NonNull Long id) {
        Storage found = storagesRepository.getById(id);
        if (ObjectUtils.isEmpty(found)) {
            log.error("Store {} unknown", id);
            throw new ResourceNotFoundException("cannot find store with id: " + id);
        }
        return StoragesConverters.toDTO(found);
    }

    public void delete(@NonNull long id) {
        if (!storagesRepository.existsById(id)) {
            log.error("failed to delete store {} store's id unknown", id);
            throw new ResourceNotFoundException("store's id unknown");
        }
        log.warn("delete store {}", id);
        storagesRepository.deleteById(id);
    }

    public boolean existByName(String name) {
        return storagesRepository.existsByName(name);
    }
}
