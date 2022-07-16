package fr.b4.apps.common.services;

import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.entities.ProductCategory;
import fr.b4.apps.common.repositories.NutrientLevelsRepository;
import fr.b4.apps.common.repositories.ProductRepository;
import fr.b4.apps.common.util.converters.ProductCategoryConverter;
import fr.b4.apps.common.util.converters.ProductConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class ProductService {

    @Value("${products.photos.dir.bill}")
    private String productsPhotoDir;

    private final ProductRepository productRepository;
    private final NutrientLevelsRepository nutrientLevelsRepository;


    public ProductService(ProductRepository productRepository, NutrientLevelsRepository nutrientLevelsRepository) {
        this.productRepository = productRepository;
        this.nutrientLevelsRepository = nutrientLevelsRepository;
    }


    public List<ProductDTO> findAllDTO() {
        return findAll().stream().map(ProductConverter::toDto).collect(Collectors.toList());
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> find(String name) {
        if (StringUtils.hasLength(name)) {
            return productRepository.findByNameContains(name);
        } else {
            throw new IllegalArgumentException("product name must not be empty");
        }
    }

    public Product save(Product product) {
        nutrientLevelsRepository.save(product.getNutrientLevels());
        return productRepository.save(product);
    }

    public Product save(Product product, MultipartFile file) throws IOException {
        if (!ObjectUtils.isEmpty(file)) {
            String photoURL = productsPhotoDir + file.getOriginalFilename();
            file.transferTo(Path.of(photoURL));
            product.setPhoto(file.getOriginalFilename());
        }
        return save(product);
    }

    public ProductDTO update(ProductDTO dto) {
        Product product = productRepository.getById(dto.getId());

        List<ProductCategory> categories = dto.getProductCategories()
                .stream()
                .map(ProductCategoryConverter::valueOf)
                .collect(Collectors.toList());

        product.setProductCategories(categories);
        productRepository.save(product);
        return ProductConverter.toDto(product);
    }
}
