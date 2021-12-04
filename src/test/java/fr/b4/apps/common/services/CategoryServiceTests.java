package fr.b4.apps.common.services;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.entities.Category;
import fr.b4.apps.common.repositories.CategoryRepository;
import fr.b4.apps.openfoodfact.models.OFCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
public class CategoryServiceTests {

    @Mock
    CategoryRepository categoryRepository;

    @Test
    public void shouldSaveCategoryListSuccess() {
        List<Category> categoryList = DataGenerator.generateCategories(5);
        CategoryService categoryService = new CategoryService(categoryRepository);
        categoryService.saveAll(categoryList);
        Mockito.verify(categoryRepository, Mockito.times(5)).save(Mockito.any());
    }

    @Test
    public void shouldSaveCategoryInDBIfUnknown() {
        Category category = DataGenerator.generateOneCategory(56);
        CategoryService categoryService = new CategoryService(categoryRepository);
        categoryService.save(category);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void shouldNotSaveCategoryInDBKnownAndHasNoContent() {
        Mockito.when(categoryRepository.existsById("56")).thenReturn(true);
        Category category = DataGenerator.generateOneCategory(56);
        category.setFr("");
        category.setEn("");
        CategoryService categoryService = new CategoryService(categoryRepository);
        categoryService.save(category);
        Mockito.verify(categoryRepository, Mockito.times(0)).save(Mockito.any());
    }


    @Test
    public void shouldSaveCategoryInDBKnownAndHasContent() {
        Mockito.when(categoryRepository.existsById("56")).thenReturn(true);
        Category category = DataGenerator.generateOneCategory(56);
        CategoryService categoryService = new CategoryService(categoryRepository);
        categoryService.save(category);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void shouldSaveOFCategorySuccess() {
        OFCategory ofCategory = DataGenerator.generateOneOFCategory(56);
        CategoryService categoryService = new CategoryService(categoryRepository);
        categoryService.updateCategory("56", ofCategory);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void shouldSaveOFCategoryAndChildrenSuccess() {
        OFCategory ofCategory = DataGenerator.generateOneOFCategory(56);
        ofCategory.setChildren(DataGenerator.generateStrings(3));
        CategoryService categoryService = new CategoryService(categoryRepository);
        categoryService.updateCategory("56", ofCategory);
        Mockito.verify(categoryRepository, Mockito.times(4)).save(Mockito.any());
    }

    @Test
    public void shouldSaveOFCategoryAndParentSuccess() {
        OFCategory ofCategory = DataGenerator.generateOneOFCategory(56);
        ofCategory.setParents(DataGenerator.generateStrings(3));
        CategoryService categoryService = new CategoryService(categoryRepository);
        categoryService.updateCategory("56", ofCategory);
        Mockito.verify(categoryRepository, Mockito.times(4)).save(Mockito.any());
    }

}
