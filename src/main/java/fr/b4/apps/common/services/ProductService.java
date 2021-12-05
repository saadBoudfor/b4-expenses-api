package fr.b4.apps.common.services;

import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.NutrientLevels;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.NutrientLevelsRepository;
import fr.b4.apps.common.repositories.ProductRepository;
import fr.b4.apps.common.util.converters.ProductConverter;
import fr.b4.apps.openfoodfact.apis.OpenFoodFactClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class ProductService {
    @Value("${working.dir}")
    private String workingDir;

    @Value("${products.photos.dir}")
    private String productsPhotoDir;

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

}
