package fr.b4.apps.common.services;

import fr.b4.apps.common.entities.Category;
import fr.b4.apps.common.repositories.CategoryRepository;
import fr.b4.apps.openfoodfact.models.OFCategory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static fr.b4.apps.common.util.converters.CategoryConverter.convert;

@Component
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void updateCategory(String key, OFCategory ofCategory) {
        Category created = convert(key, ofCategory);
        if (!CollectionUtils.isEmpty(created.getChildren()))
            this.saveAll(created.getChildren());
        if (!CollectionUtils.isEmpty(created.getParents()))
            this.saveAll(created.getParents());
        this.save(created);
    }

    public void saveAll(List<Category> list) {
        list.forEach(this::save);
    }

    public void save(Category created) {
        if (categoryRepository.existsById(created.getId())) {
            if (StringUtils.hasLength(created.getEn()) || StringUtils.hasLength(created.getFr())) {
                categoryRepository.save(created);
            }
        } else {
            // si il n'existe pas, on le crée
            categoryRepository.save(created);
        }

    }

}
