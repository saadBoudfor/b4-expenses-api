package fr.b4.apps.stores.web;

import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.stores.dto.BucketDTO;
import fr.b4.apps.stores.services.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("buckets")
@RestController
public class BucketController {

    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping
    public BucketDTO addBucket(@RequestBody BucketDTO bucket) throws BadRequestException {
        if (ObjectUtils.isNotEmpty(bucket.getId())) {
            log.error("Bucket's id must be null");
            throw new BadRequestException("Bucket's id must be null");
        }
        checkBucketDTOValidity(bucket);
        log.info("create new bucket to store {} (user: {})", bucket.getStore().getId(), bucket.getOwner().getId());
        return bucketService.save(bucket);
    }

    @PutMapping
    public BucketDTO update(@RequestBody BucketDTO bucket) {
        if (ObjectUtils.isEmpty(bucket.getId())) {
            log.error("Bucket's id must not be null");
            throw new BadRequestException("Bucket's id must not be null");
        }
        checkBucketDTOValidity(bucket);
        log.info("update bucket {}", bucket);

        return bucketService.update(bucket);
    }

    @DeleteMapping("/{bucketID}")
    public void delete(@PathVariable("bucketID") Long id) {
        if (ObjectUtils.isEmpty(id) || id <= 0) {
            log.error("bucket's id is invalid: {}", id);
            throw new IllegalArgumentException("bucket's id is invalid");
        }
        bucketService.delete(id);
    }

    @GetMapping
    public List<BucketDTO> get(@RequestParam(value = "store", required = false) @Nullable Long storeID,
                               @RequestParam(value = "user", required = false) @Nullable Long userId)
            throws BadRequestException, IllegalArgumentException {

        if (ObjectUtils.isEmpty(storeID) && ObjectUtils.isEmpty(userId)) {
            log.error("Must define at least on filter by store or user (store: {}, user: {})", storeID, userId);
            throw new BadRequestException("Must define at least on filter by store or by user");
        } else {
            if (ObjectUtils.isNotEmpty(storeID) && storeID <= 0) {
                log.error("Store's id is invalid: {}", storeID);
                throw new IllegalArgumentException("Store's id is invalid");
            }

            if (ObjectUtils.isNotEmpty(userId) && userId <= 0) {
                log.error("User's id is invalid: {}", userId);
                throw new IllegalArgumentException("User's id is invalid");
            }

            if (ObjectUtils.isNotEmpty(storeID)) {
                return bucketService.filterByStoreId(storeID);
            } else {
                assert ObjectUtils.isNotEmpty(userId);
                return bucketService.filterByUserId(userId);
            }

        }
    }

    private static void checkBucketDTOValidity(BucketDTO bucket) throws BadRequestException {
        if (!StringUtils.hasLength(bucket.getName())) {
            log.error("name cannot be empty");
            throw new BadRequestException("name cannot be empty");
        }
        if (ObjectUtils.isEmpty(bucket.getStore()) || ObjectUtils.isEmpty(bucket.getStore().getId())) {
            log.error("store (or store's id) is missing");
            throw new BadRequestException("store (or store's id) is missing");
        }
        if (ObjectUtils.isEmpty(bucket.getOwner()) || ObjectUtils.isEmpty(bucket.getOwner().getId())) {
            log.error("Owner (or Owner's id) is missing");
            throw new BadRequestException("Owner (or Owner's id) is missing");
        }
    }
}
