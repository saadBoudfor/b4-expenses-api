package fr.b4.apps.common.util.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.b4.apps.common.dto.NutrientLevelsDTO;
import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.NutrientLevels;
import fr.b4.apps.common.entities.Product;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@UtilityClass
public class ProductConverter {
    public static Product valueOf(String str) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(str, Product.class);
    }

    public static ProductDTO toDto(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setQrCode(product.getQrCode());
        dto.setComment(product.getComment());
        dto.setCalories(product.getCalories());
        dto.setPhoto(product.getPhoto());
        dto.setUnit(product.getUnit());
        dto.setDisplayQuantity(product.getDisplayQuantity());
        dto.setBrand(product.getBrand());
        dto.setDataPer(product.getDataPer());
        if(!ObjectUtils.isEmpty(product.getNutrientLevels())) {
            dto.setNutrientLevels(toDto(product.getNutrientLevels()));
        }
        dto.setScore(product.getScore());
//        dto.setCategories(product.getCategories());
        return dto;
    }

    public static NutrientLevelsDTO toDto(NutrientLevels nutrientLevels) {
        NutrientLevelsDTO dto = new NutrientLevelsDTO();
        dto.setFat(nutrientLevels.getFat());
        dto.setSugars(nutrientLevels.getSugars());
        dto.setSaturatedFat(nutrientLevels.getSaturatedFat());
        dto.setSalt(nutrientLevels.getSalt());
        return dto;
    }
}
