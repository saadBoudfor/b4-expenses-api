package fr.b4.apps.stores.process;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.expenses.entities.Expense;
import fr.b4.apps.expenses.repositories.ExpenseRepository;
import fr.b4.apps.expenses.services.ExpenseService;
import fr.b4.apps.stores.dto.ItemDTO;
import fr.b4.apps.stores.entities.Bucket;
import fr.b4.apps.stores.entities.Item;
import fr.b4.apps.stores.repositories.BucketRepository;
import fr.b4.apps.stores.repositories.ItemRepository;
import fr.b4.apps.stores.util.converters.ItemConverter;
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

    public ItemProcess(BucketRepository bucketRepository,
                       UserRepository userRepository,
                       ExpenseRepository expenseRepository, ItemRepository itemRepository, ExpenseService expenseService) {
        this.bucketRepository = bucketRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.itemRepository = itemRepository;
        this.expenseService = expenseService;
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

        item.setLocation(location);
        item.setAuthor(author);

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
        } else {
            log.error("Failed to save Item: expense cannot be empty: {}", itemDTO);
            throw new BadRequestException("Failed to save Item: expense cannot be empty");
        }

        Item saved = itemRepository.save(item);
        return ItemConverter.toDTO(saved);
    }
}