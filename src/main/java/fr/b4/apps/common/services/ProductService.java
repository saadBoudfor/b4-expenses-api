package fr.b4.apps.common.services;

import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.ProductRepository;
import fr.b4.apps.openfoodfact.apis.OpenFoodFactClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class ProductService {
    @Value("${working.dir}")
    private String workingDir;

    @Value("${products.photos.dir}")
    private String productsPhotoDir;

    private final ProductRepository productRepository;
    private final OpenFoodFactClient openFoodFactClient;

    public ProductService(ProductRepository productRepository, OpenFoodFactClient openFoodFactClient) {
        this.productRepository = productRepository;
        this.openFoodFactClient = openFoodFactClient;
    }

    public List<Product> find(String name) {
        log.info("search product {}", name);
        List<Product> products = new ArrayList<>();
        if (StringUtils.hasLength(name))
            execLocalSearch(name, products);
        products.addAll(openFoodFactClient.search(name));
        return products;
    }

    private void execLocalSearch(String name, List<Product> products) {
        List<Product> localSearchResult = productRepository.findByNameContains(name);
        if (!CollectionUtils.isEmpty(localSearchResult))
            products.addAll(localSearchResult);
    }

    public Product searchByCode(String barCode) {
        return openFoodFactClient.searchByCode(barCode);
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
