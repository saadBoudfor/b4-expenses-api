package fr.b4.apps.common;

import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.NutrientLevels;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.NutrientLevelsRepository;
import fr.b4.apps.common.services.ProductService;
import fr.b4.apps.common.util.converters.ProductConverter;
import fr.b4.apps.openfoodfact.apis.OpenFoodFactClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductProcess {

    private final ProductService productService;
    private final OpenFoodFactClient openFoodFactClient;
    private final NutrientLevelsRepository nutrientLevelsRepository;

    public ProductProcess(ProductService productService,
                          OpenFoodFactClient openFoodFactClient,
                          NutrientLevelsRepository nutrientLevelsRepository) {
        this.productService = productService;
        this.openFoodFactClient = openFoodFactClient;
        this.nutrientLevelsRepository = nutrientLevelsRepository;
    }

    public List<ProductDTO> find(String name) {
        log.info("search product {}", name);
        List<Product> products = new ArrayList<>();
        if (StringUtils.hasLength(name)) {
            products.addAll(productService.find(name));
        }
        products.addAll(openFoodFactClient.search(name));
        return products.stream().map(ProductConverter::toDto).collect(Collectors.toList());
    }


    public ProductDTO searchByCode(String barCode) {
        return ProductConverter.toDto(openFoodFactClient.searchByCode(barCode));
    }

    @PostConstruct
    public void updateProduct() {
        try {
            List<Product> products = productService.findAll();
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
                        productService.save(product);
                    }
                }
            });
        } catch (ResourceAccessException exception) {
            log.error("failed perform search request on Open Food Fact API. Error: {}", exception.getMessage());
        }
    }

    private static void updateNutrimentScore(Product found, Product product) {
        product.setScore(found.getScore());
        product.setBrand(found.getBrand());
        product.setCalories(found.getCalories());
        product.setDataPer(found.getDataPer());
        product.setQuantity(found.getQuantity());
    }

    private void updateNutrimentLevels(Product found, Product product) {
        if (!ObjectUtils.isEmpty(found.getNutrientLevels())) {
            NutrientLevels saved = nutrientLevelsRepository.save(found.getNutrientLevels());
            product.setNutrientLevels(saved);
        }
    }
}
