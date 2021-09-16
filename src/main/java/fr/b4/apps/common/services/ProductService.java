package fr.b4.apps.common.services;

import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.ProductRepository;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.util.List;


@Slf4j
@Component
public class ProductService {
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
}
