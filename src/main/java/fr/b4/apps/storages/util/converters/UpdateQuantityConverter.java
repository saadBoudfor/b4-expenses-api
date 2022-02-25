package fr.b4.apps.storages.util.converters;

import fr.b4.apps.storages.dto.UpdateQuantity;
import fr.b4.apps.storages.dto.UpdateQuantityDTO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateQuantityConverter {
    public static UpdateQuantity valueOf(UpdateQuantityDTO dto) {
        UpdateQuantity updateQuantity = new UpdateQuantity();
        updateQuantity.setQuantity(dto.getQuantity());
        updateQuantity.setComment(dto.getComment());
        updateQuantity.setAuthor(dto.getAuthor());
        updateQuantity.setId(dto.getId());
        return updateQuantity;
    }

    public static UpdateQuantityDTO toDTO(UpdateQuantity updateQuantity) {
        UpdateQuantityDTO dto = new UpdateQuantityDTO();
        dto.setQuantity(updateQuantity.getQuantity());
        dto.setComment(updateQuantity.getComment());
        dto.setAuthor(updateQuantity.getAuthor());
        dto.setId(updateQuantity.getId());
        dto.setUpdateTime(updateQuantity.getUpdateTime());
        return dto;
    }

    public static List<UpdateQuantityDTO> toDTO(List<UpdateQuantity> updateQuantityList) {
        if(CollectionUtils.isEmpty(updateQuantityList)) return new ArrayList<>();
        return updateQuantityList.stream().map(UpdateQuantityConverter::toDTO).collect(Collectors.toList());
    }
}
