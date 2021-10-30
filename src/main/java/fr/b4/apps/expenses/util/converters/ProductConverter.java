package fr.b4.apps.expenses.util.converters;

import fr.b4.apps.common.entities.Category;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.expenses.dto.ProductDTO;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;

@UtilityClass
public class ProductConverter {
    public static ProductDTO toDTO(Product product) {
        if (ObjectUtils.isEmpty(product))
            return null;
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setQuantity(product.getQuantity());
        dto.setName(product.getName());
        dto.setQrCode(product.getQrCode());
        dto.setCalories(product.getCalories());
        dto.setPhoto(product.getPhoto());
        dto.setUnit(product.getUnit());
        dto.setDisplayQuantity(product.getDisplayQuantity());
        dto.setBrand(product.getBrand());
        dto.setDataPer(product.getDataPer());
        dto.setComment(product.getComment());
        if (!CollectionUtils.isEmpty(product.getCategories()))
            dto.setCategories(product.getCategories()
                    .stream()
                    .map(Category::getFr)
                    .collect(Collectors.toList()));
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
