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
    private final OpenFoodFactClient openFoodFactClient;
    private final NutrientLevelsRepository nutrientLevelsRepository;

    public ProductService(ProductRepository productRepository,
                          OpenFoodFactClient openFoodFactClient,
                          NutrientLevelsRepository nutrientLevelsRepository) {
        this.productRepository = productRepository;
        this.openFoodFactClient = openFoodFactClient;
        this.nutrientLevelsRepository = nutrientLevelsRepository;
    }

    @PostConstruct
    public void updateProduct() {
        List<Product> products = productRepository.findAll();
        products.forEach(product -> {
            if (ObjectUtils.isEmpty(product.getQrCode())) {
                log.warn("cannot update product {}, barCode missing", product.getName());
            } else {
                log.info("check for local product update: {} ({})", product.getName(), product.getQrCode());
                Product found = openFoodFactClient.searchByCode(product.getQrCode());
                if (!ObjectUtils.isEmpty(found)) {
                    log.info("Product {} updated", found.getQrCode());
                    updateNutrimentLevels(found, product);
                    updateNutrimentScore(found, product);
                    productRepository.save(product);
                }
            }
        });
    }

    private void updateNutrimentScore(Product found, Product product) {
        product.setScore(found.getScore());
        product.setBrand(found.getBrand());
        product.setCalories(found.getCalories());
        product.setDataPer(found.getDataPer());
        product.setQuantity(found.getQuantity());
    }

    private void updateNutrimentLevels(Product found, Product product) {
        if(!ObjectUtils.isEmpty(found.getNutrientLevels())) {
            NutrientLevels saved = nutrientLevelsRepository.save(found.getNutrientLevels());
            product.setNutrientLevels(saved);
        }
    }

    public List<ProductDTO> getAllFromDB() {
        return productRepository.findAll().stream().map(ProductConverter::toDto).collect(Collectors.toList());
    }

    public List<ProductDTO> find(String name) {
        log.info("search product {}", name);
        List<Product> products = new ArrayList<>();
        if (StringUtils.hasLength(name))
            execLocalSearch(name, products);
        products.addAll(openFoodFactClient.search(name));
        return products.stream().map(ProductConverter::toDto).collect(Collectors.toList());
    }

    private void execLocalSearch(String name, List<Product> products) {
        List<Product> localSearchResult = productRepository.findByNameContains(name);
        if (!CollectionUtils.isEmpty(localSearchResult))
            products.addAll(localSearchResult);
    }

    public ProductDTO searchByCode(String barCode) {
        return ProductConverter.toDto(openFoodFactClient.searchByCode(barCode));
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
