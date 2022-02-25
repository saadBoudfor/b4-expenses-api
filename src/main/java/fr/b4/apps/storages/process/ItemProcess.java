package fr.b4.apps.storages.process;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.services.ProductService;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.storages.dto.ItemDTO;
import fr.b4.apps.storages.dto.UpdateQuantity;
import fr.b4.apps.storages.dto.UpdateQuantityDTO;
import fr.b4.apps.storages.entities.Bucket;
import fr.b4.apps.storages.entities.Item;
import fr.b4.apps.storages.repositories.BucketRepository;
import fr.b4.apps.storages.repositories.ItemRepository;
import fr.b4.apps.storages.repositories.UpdateQuantityRepository;
import fr.b4.apps.storages.util.converters.ItemConverter;
import fr.b4.apps.storages.util.converters.UpdateQuantityConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItemProcess {

    private final BucketRepository bucketRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final ItemRepository itemRepository;
    private final ExpenseService expenseService;
    private final ProductService productService;
    private final UpdateQuantityRepository updateQuantityRepository;

    public ItemProcess(BucketRepository bucketRepository,
                       UserRepository userRepository,
                       ExpenseRepository expenseRepository, ItemRepository itemRepository, ExpenseService expenseService, ProductService productService, UpdateQuantityRepository updateQuantityRepository) {
        this.bucketRepository = bucketRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.itemRepository = itemRepository;
        this.expenseService = expenseService;
        this.productService = productService;
        this.updateQuantityRepository = updateQuantityRepository;
    }

    public ItemDTO save(ItemDTO itemDTO) {
        Item item = ItemConverter.toItem(itemDTO);
        final Long locationId = itemDTO.getLocation().getId();
        final Long userId = itemDTO.getAuthor().getId();

        // get dependencies
        Bucket location = bucketRepository.getById(locationId);
        User author = userRepository.getById(userId);

        // check dependencies validity
        if (ObjectUtils.isEmpty(location)) {
            log.error("Failed to save Item: location {} is unknown", locationId);
            throw new BadRequestException("Failed to save Item: given location is unknown");
        }

        if (ObjectUtils.isEmpty(author)) {
            log.error("Failed to save Item: author {} is unknown", userId);
            throw new BadRequestException("Failed to save Item: given author is unknown");
        }

        if (ObjectUtils.isNotEmpty(itemDTO.getProduct())
                && ObjectUtils.isEmpty(itemDTO.getProduct().getId())) {
            Product savedProduct = this.productService.save(item.getProduct());
            item.setProduct(savedProduct);
        }

        if (ObjectUtils.isNotEmpty(itemDTO.getExpense())) {
            final Expense expense;
            final Long expenseId = itemDTO.getExpense().getId();
            if (ObjectUtils.isNotEmpty(expenseId)) {
                expense = expenseRepository.getById(expenseId);
                if (ObjectUtils.isEmpty(expense)) {
                    log.error("Failed to save Item: expense {} is unknown", expenseId);
                    throw new BadRequestException("Failed to save Item: given expense is unknown");
                }
            } else {
                expense = expenseService.save(itemDTO.getExpense());
            }
            item.setExpense(expense);
        } 

        Item saved = itemRepository.save(item);
        return ItemConverter.toDTO(saved);
    }

    public UpdateQuantityDTO updateQuantity(Long itemId, UpdateQuantityDTO dto) {
        Item item = itemRepository.getById(itemId);

        UpdateQuantity updateQuantity = UpdateQuantityConverter.valueOf(dto);
        updateQuantity.setItem(item);

        UpdateQuantity saved = updateQuantityRepository.save(updateQuantity);
        item.setRemaining(updateQuantity.getQuantity());
        itemRepository.save(item);
        return UpdateQuantityConverter.toDTO(saved);
    }
}
