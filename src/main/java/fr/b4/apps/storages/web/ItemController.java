package fr.b4.apps.storages.web;

import fr.b4.apps.common.exceptions.ForbiddenException;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import fr.b4.apps.storages.dto.ItemDTO;
import fr.b4.apps.storages.process.ItemProcess;
import fr.b4.apps.storages.services.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("items")
@RestController
public class ItemController {
    private final ItemService itemService;
    private final ItemProcess itemProcess;

    public ItemController(ItemService itemService,
                          ItemProcess itemProcess) {
        this.itemService = itemService;
        this.itemProcess = itemProcess;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemDTO save(@RequestBody ItemDTO itemDTO) {
        if (ObjectUtils.isNotEmpty(itemDTO.getId())) {
            log.error("id must be null (trying to create new item with existing id)");
            throw new IllegalArgumentException("id must be null (trying to create new item with existing id)");
        }
        checkRequiredFields(itemDTO);
        log.info("add new item \"{}\" to bucket {}", itemDTO.getProduct().getName(), itemDTO.getLocation().getId());
        return itemProcess.save(itemDTO);
    }

    @PutMapping
    public ItemDTO update(@RequestBody ItemDTO itemDTO) {
        if (ObjectUtils.isEmpty(itemDTO.getId())) {
            log.error("Failed to update item {} id is null", itemDTO);
            throw new IllegalArgumentException("Failed to update. Id is missing");
        }
        checkRequiredFields(itemDTO);
        log.info("update item \"{}\"  (bucket {})", itemDTO.getExpense().getName(), itemDTO.getLocation().getId());
        return itemService.update(itemDTO);
    }

    @GetMapping
    public List<ItemDTO> get(@RequestParam(value = "user", required = false) Long userId,
                             @RequestParam(value = "bucket", required = false) Long bucketId) {
        if (ObjectUtils.isEmpty(bucketId) && ObjectUtils.isEmpty(userId)) {
            log.error("Must define at least on filter by Bucket or user (store: {}, user: {})", bucketId, userId);
            throw new IllegalArgumentException("Must define at least on filter by Bucket or by user");
        } else {
            if (ObjectUtils.isNotEmpty(bucketId) && bucketId <= 0) {
                log.error("Bucket's id is invalid: {}", bucketId);
                throw new IllegalArgumentException("Bucket's id is invalid");
            }

            if (ObjectUtils.isNotEmpty(userId) && userId <= 0) {
                log.error("User's id is invalid: {}", userId);
                throw new IllegalArgumentException("User's id is invalid");
            }


            if (ObjectUtils.isNotEmpty(bucketId)) {
                return itemService.filterByLocationId(bucketId);
            } else {
                assert ObjectUtils.isNotEmpty(userId);
                return itemService.filterByAuthorId(userId);
            }
        }
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable("itemId") Long itemId) {
        if (ObjectUtils.isEmpty(itemId) || itemId <= 0) {
            log.error("Item's id is invalid: {}", itemId);
            throw new IllegalArgumentException("Item's id is invalid");
        }
        itemService.delete(itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDTO getById(@PathVariable("itemId") Long itemId) {
        return itemService.getById(itemId);
    }

    private void checkRequiredFields(ItemDTO item) {
        List<String> invalidField = new ArrayList<>();

        if (ObjectUtils.isEmpty(item.getLocation()) || ObjectUtils.isEmpty(item.getLocation().getId())) {
            log.error("required location missing or invalid");
            invalidField.add("location");
        }

        if (ObjectUtils.isEmpty(item.getAuthor()) || ObjectUtils.isEmpty(item.getAuthor().getId())) {
            log.error("required author missing or invalid");
            invalidField.add("author");
        }

        if (ObjectUtils.isEmpty(item.getQuantity())) {
            log.error("required quantity missing or invalid");
            invalidField.add("quantity");
        }

        if (ObjectUtils.isEmpty(item.getRemaining())) {
            log.error("required remaining quantity missing or invalid");
            invalidField.add("remaining quantity");
        }

        if (CollectionUtils.isNotEmpty(invalidField)) {
            throw new IllegalArgumentException("The following required fields are missing or invalid: "
                    + String.join(", ", invalidField));
        }
    }

    private boolean isExpenseInvalid(ExpenseDTO expense) {
        final boolean isNewExpenseWithoutName = expense != null
                && expense.getId() == null && StringUtils.isEmpty(expense.getName());
        return ObjectUtils.isEmpty(expense) || isNewExpenseWithoutName;
    }

}
