package fr.b4.apps.common.util.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.b4.apps.common.dto.ProductCategoryDTO;
import fr.b4.apps.common.entities.ProductCategory;
import fr.b4.apps.expenses.dto.ExpenseDTO;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ProductCategoryConverter {
    public ProductCategory valueOf(ProductCategoryDTO dto) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setLabel(dto.getLabel());
        productCategory.setImgURL(dto.getImgURL());
        return productCategory;
    }

    public ProductCategory valueOf(String dto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(dto, ProductCategory.class);
    }

    public ProductCategoryDTO toDTO(ProductCategory productCategory) {
        ProductCategoryDTO dto = new ProductCategoryDTO();
        dto.setLabel(productCategory.getLabel());
        dto.setImgURL(productCategory.getImgURL());
        return dto;
    }

    public List<ProductCategory> valueOf(List<ProductCategoryDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) return new ArrayList<>();
        return dtos.stream().map(ProductCategoryConverter::valueOf).collect(Collectors.toList());
    }

    public List<ProductCategoryDTO> toDTO(List<ProductCategory> productCategories) {
        if (CollectionUtils.isEmpty(productCategories)) return new ArrayList<>();
        return productCategories.stream().map(ProductCategoryConverter::toDTO).collect(Collectors.toList());
    }
}
