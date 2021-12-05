package fr.b4.apps.common.util.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.b4.apps.common.dto.NutrientLevelsDTO;
import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.Category;
import fr.b4.apps.common.entities.NutrientLevels;
import fr.b4.apps.common.entities.Product;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ProductConverter {
    public static Product valueOf(String str) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(str, Product.class);
    }

    public static List<ProductDTO> toDto(@Nullable List<Product> products) {
        if (CollectionUtils.isEmpty(products)) {
            return new ArrayList<>();
        }
        return products.stream().map(ProductConverter::toDto).collect(Collectors.toList());
    }

    public static ProductDTO toDto(@Nullable Product product) {
        if (ObjectUtils.isEmpty(product)) {
            return null;
        }
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
        if (!ObjectUtils.isEmpty(product.getNutrientLevels())) {
            dto.setNutrientLevels(toDto(product.getNutrientLevels()));
        }
        dto.setScore(product.getScore());
        if (!CollectionUtils.isEmpty(product.getCategories()))
            dto.setCategories(product.getCategories()
                    .stream()
                    .map(Category::getFr)
                    .collect(Collectors.toList()));
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

    public static Product toProduct(ProductDTO dto) {
        if (ObjectUtils.isEmpty(dto))
            return null;
        Product product = new Product();
        product.setId(dto.getId());
        product.setQuantity(dto.getQuantity());
        product.setName(dto.getName());
        product.setQrCode(dto.getQrCode());
        product.setCalories(dto.getCalories());
        product.setPhoto(dto.getPhoto());
        product.setUnit(dto.getUnit());
        product.setDisplayQuantity(dto.getDisplayQuantity());
        product.setBrand(dto.getBrand());
        product.setDataPer(dto.getDataPer());
        product.setComment(dto.getComment());
        return product;
    }
}
