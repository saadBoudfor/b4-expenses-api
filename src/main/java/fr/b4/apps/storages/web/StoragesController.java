package fr.b4.apps.storages.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.expenses.dto.MessageDTO;
import fr.b4.apps.storages.dto.StorageDTO;
import fr.b4.apps.storages.process.StorageProcess;
import fr.b4.apps.storages.services.StoragesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Define web services to manage stores
 */
@Slf4j
@RequestMapping("storages")
@RestController
public class StoragesController {

    private final StoragesService storagesService;
    private final StorageProcess storageProcess;

    public StoragesController(StoragesService storagesService,
                              StorageProcess storageProcess) {
        this.storagesService = storagesService;
        this.storageProcess = storageProcess;
    }

    @PostMapping
    public StorageDTO addNewStore(@RequestBody StorageDTO storageDTO) {
        log.info("add new Store: {}", storageDTO);
        if (ObjectUtils.isEmpty(storageDTO)) {
            throw new BadRequestException("store must not be empty");
        }

        if (ObjectUtils.isEmpty(storageDTO.getOwner())) {
            throw new BadRequestException("store's owner is required");
        }

        if (CollectionUtils.isEmpty(storageDTO.getBuckets())) {
            throw new BadRequestException("store must have at least one bucket");
        }

        return storageProcess.save(storageDTO);
    }

    @PutMapping
    public StorageDTO updateStore(@RequestBody StorageDTO storageDTO) {
        log.info("update Store: {}", storageDTO);
        if (ObjectUtils.isEmpty(storageDTO) || ObjectUtils.isEmpty(storageDTO.getId())) {
            throw new IllegalArgumentException("store and store id must not be empty");
        }
        return storagesService.update(storageDTO);
    }

    @GetMapping("/{id}")
    public StorageDTO get(@PathVariable("id") Long id) {
        log.debug("Get store {} information", id);
        if (ObjectUtils.isEmpty(id) || id <= 0) {
            throw new IllegalArgumentException("invalid store id");
        }
        return storagesService.getByID(id);
    }

    @GetMapping("/all/{userID}")
    public List<StorageDTO> getByUserID(@PathVariable("userID") Long id) {
        log.debug("Get user {} stores", id);
        if (ObjectUtils.isEmpty(id) || id <= 0) {
            throw new IllegalArgumentException("invalid user id");
        }
        return storagesService.getByUserID(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.debug("Get user {} stores", id);
        if (ObjectUtils.isEmpty(id) || id <= 0) {
            throw new IllegalArgumentException("invalid user id");
        }
        log.warn("delete store with id: {}", id);
        storagesService.delete(id);
    }

    @GetMapping("/by-name/{name}")
    public MessageDTO existByName(@PathVariable("name") String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("cannot perfom search search storage for empty name");
        }
        return new MessageDTO(storagesService.existByName(name));
    }

}
