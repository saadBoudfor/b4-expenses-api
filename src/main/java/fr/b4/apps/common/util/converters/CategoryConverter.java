package fr.b4.apps.common.util.converters;

import fr.b4.apps.common.entities.Category;
import fr.b4.apps.common.services.CategoryService;
import fr.b4.apps.openfoodfact.models.OFCategory;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@UtilityClass
public class CategoryConverter {

    public static Category convert(String key, OFCategory ofCategory) {
        Category category = new Category();
        category.setId(key);
        category.setEn(ofCategory.getName().getEn());
        category.setFr(ofCategory.getName().getFr());
        if (!CollectionUtils.isEmpty(ofCategory.getParents()))
            category.setParents(ofCategory.getParents().stream().map(CategoryConverter::convert).collect(Collectors.toList()));
        if (!CollectionUtils.isEmpty(ofCategory.getChildren()))
            category.setParents(ofCategory.getChildren().stream().map(CategoryConverter::convert).collect(Collectors.toList()));
        return category;
    }

    public static Category convert(String key) {
        Category category = new Category();
        category.setId(key);
        return category;
    }

}
