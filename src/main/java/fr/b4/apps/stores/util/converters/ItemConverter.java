package fr.b4.apps.stores.util.converters;

import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import fr.b4.apps.stores.dto.ItemDTO;
import fr.b4.apps.stores.entities.Item;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

@UtilityClass
public class ItemConverter {
    public static Item toItem(ItemDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return null;
        }
        Item item = new Item();
        item.setId(dto.getId());
        item.setExpense(ExpenseConverter.toExpense(dto.getExpense()));
        item.setLocation(BucketConverter.toBucket(dto.getLocation()));
        item.setQuantity(dto.getQuantity());
        item.setRemaining(dto.getRemaining());
        return item;
    }

    public static ItemDTO toDTO(Item item) {
        if (ObjectUtils.isEmpty(item)) {
            return null;
        }
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setExpense(ExpenseConverter.toDTO(item.getExpense()));
        dto.setLocation(BucketConverter.toDTO(item.getLocation()));
        dto.setQuantity(item.getQuantity());
        dto.setRemaining(item.getRemaining());
        return dto;
    }
}
