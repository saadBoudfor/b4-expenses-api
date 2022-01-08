package fr.b4.apps.stores.web;

import fr.b4.apps.stores.dto.StoreDTO;
import fr.b4.apps.stores.services.StoresService;
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
public class StoresController {

    private final StoresService storesService;

    public StoresController(StoresService storesService) {
        this.storesService = storesService;
    }

    @PostMapping
    public StoreDTO addNewStore(@RequestBody StoreDTO storeDTO) {
        log.info("add new Store: {}", storeDTO);
        if (ObjectUtils.isEmpty(storeDTO) || ObjectUtils.isEmpty(storeDTO.getOwner())) {
            throw new IllegalArgumentException("store must not be empty");
        }
        return storesService.save(storeDTO);
    }

    @PutMapping
    public StoreDTO updateStore(@RequestBody StoreDTO storeDTO) {
        log.info("update Store: {}", storeDTO);
        if (ObjectUtils.isEmpty(storeDTO) || ObjectUtils.isEmpty(storeDTO.getId())) {
            throw new IllegalArgumentException("store and store id must not be empty");
        }
        return storesService.update(storeDTO);
    }

    @GetMapping("/{id}")
    public StoreDTO get(@PathVariable("id") Long id) {
        log.debug("Get store {} information", id);
        if (ObjectUtils.isEmpty(id) || id <= 0) {
            throw new IllegalArgumentException("invalid store id");
        }
        return storesService.getByID(id);
    }

    @GetMapping("/all/{userID}")
    public List<StoreDTO> getByUserID(@PathVariable("userID") Long id) {
        log.debug("Get user {} stores", id);
        if (ObjectUtils.isEmpty(id) || id <= 0) {
            throw new IllegalArgumentException("invalid user id");
        }
        return storesService.getByUserID(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.debug("Get user {} stores", id);
        if (ObjectUtils.isEmpty(id) || id <= 0) {
            throw new IllegalArgumentException("invalid user id");
        }
        log.warn("delete store with id: {}", id);
        storesService.delete(id);
    }

}
