package fr.b4.apps.common.services;

import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.ProductRepository;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


@Slf4j
@Component
public class ProductService {
    @Value("${working.dir}")
    String workingDir;

    @Value("${products.photos.dir}")
    String productsPhotoDir;

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> find(String name) {
        log.info("search product {}", name);
        if (StringUtils.hasLength(name))
            return productRepository.findByNameContains(name);
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product save(Product product, MultipartFile file) throws IOException {
        if (!ObjectUtils.isEmpty(file)) {
            String photoURL = workingDir + productsPhotoDir + file.getOriginalFilename();
            file.transferTo(Path.of(photoURL));
            product.setPhoto(file.getOriginalFilename());
        }
        return save(product);
    }
}
