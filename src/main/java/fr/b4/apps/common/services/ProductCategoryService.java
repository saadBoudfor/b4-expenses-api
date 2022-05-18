package fr.b4.apps.common.services;

import fr.b4.apps.common.dto.ProductCategoryDTO;
import fr.b4.apps.common.entities.ProductCategory;
import fr.b4.apps.common.repositories.ProductCategoryRepository;
import fr.b4.apps.common.util.converters.ProductCategoryConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Component
public class ProductCategoryService {


    @Value("${products.categories.photos.dir}")
    private String productCategoryFolder;

    private final ProductCategoryRepository repository;

    public ProductCategoryService(ProductCategoryRepository repository) {
        this.repository = repository;
    }

    public List<ProductCategoryDTO> getAllCategories() {
        return ProductCategoryConverter.toDTO(this.repository.findAll());
    }

    public ProductCategoryDTO save(ProductCategory productCategory, MultipartFile file) throws IOException {
        productCategory.setImgURL(file.getOriginalFilename());

        file.transferTo(Path.of(productCategoryFolder + '/' + file.getOriginalFilename()));

        repository.save(productCategory);
        return ProductCategoryConverter.toDTO(productCategory);
    }

}
