package fr.b4.apps.storages.util.converters;

import fr.b4.apps.common.util.converters.ProductConverter;
import fr.b4.apps.expenses.util.converters.ExpenseConverter;
import fr.b4.apps.storages.dto.DurationDTO;
import fr.b4.apps.storages.dto.ItemDTO;
import fr.b4.apps.storages.entities.Item;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemConverter {
    public static Item toItem(ItemDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return null;
        }
        Item item = new Item();
        item.setId(dto.getId());
        item.setExpense(ExpenseConverter.toExpense(dto.getExpense()));
        if (ObjectUtils.isNotEmpty(dto.getLocation())) {
            item.setLocation(BucketConverter.toBucket(dto.getLocation()));
        }
        item.setProduct(ProductConverter.toProduct(dto.getProduct()));
        item.setQuantity(dto.getQuantity());
        item.setRemaining(dto.getRemaining());
        item.setAuthor(dto.getAuthor());
        item.setExpirationDate(dto.getExpirationDate());
        item.setAddDate(dto.getAddDate());
        item.setOpenDate(dto.getOpenDate());

        if (ObjectUtils.isNotEmpty(dto.getExpirationAfter())) {
            item.setExpirationAfterDays(dto.getExpirationAfter().getDays());
            item.setExpirationAfterHours(dto.getExpirationAfter().getHours());
            item.setExpirationAfterMinutes(dto.getExpirationAfter().getMinutes());
        }
        return item;
    }

    public static ItemDTO toDTO(Item item) {
        if (ObjectUtils.isEmpty(item)) {
            return null;
        }
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setAuthor(item.getAuthor());
        dto.setExpense(ExpenseConverter.toDTO(item.getExpense()));
        dto.setLocation(BucketConverter.toDTO(item.getLocation()));
        dto.setQuantity(item.getQuantity());
        dto.setRemaining(item.getRemaining());
        dto.setProduct(ProductConverter.toDto(item.getProduct()));
        dto.setExpirationDate(item.getExpirationDate());
        dto.setAddDate(item.getAddDate());
        dto.setOpenDate(item.getOpenDate());
        if (ObjectUtils.isNotEmpty(item.getExpirationAfterDays())
                || ObjectUtils.isNotEmpty(item.getExpirationAfterMinutes())
                || ObjectUtils.isNotEmpty(item.getExpirationAfterHours())) {
            DurationDTO duration = new DurationDTO();
            duration.setDays(item.getExpirationAfterDays());
            duration.setHours(item.getExpirationAfterHours());
            duration.setMinutes(item.getExpirationAfterMinutes());
            dto.setExpirationAfter(duration);
        }
        return dto;
    }

    public static List<ItemDTO> toDTO(List<Item> items) {
        if (CollectionUtils.isEmpty(items)) {
            return new ArrayList<>();
        }
        return items.stream().map(ItemConverter::toDTO).collect(Collectors.toList());
    }
}
