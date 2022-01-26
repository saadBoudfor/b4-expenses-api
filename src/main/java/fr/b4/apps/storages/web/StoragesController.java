package fr.b4.apps.storages.web;

import fr.b4.apps.storages.dto.StorageDTO;
import fr.b4.apps.storages.services.StoragesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Define web services to manage stores
 */
@Slf4j
@RequestMapping("stores")
@RestController
public class StoragesController {

    private final StoragesService storagesService;

    public StoragesController(StoragesService storagesService) {
        this.storagesService = storagesService;
    }

    @PostMapping
    public StorageDTO addNewStore(@RequestBody StorageDTO storageDTO) {
        log.info("add new Store: {}", storageDTO);
        if (ObjectUtils.isEmpty(storageDTO) || ObjectUtils.isEmpty(storageDTO.getOwner())) {
            throw new IllegalArgumentException("store must not be empty");
        }
        return storagesService.save(storageDTO);
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

}
