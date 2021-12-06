package fr.b4.apps.common.process;

import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.NutrientLevels;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.NutrientLevelsRepository;
import fr.b4.apps.common.services.ProductService;
import fr.b4.apps.common.util.converters.ProductConverter;
import fr.b4.apps.openfoodfact.apis.OpenFoodFactClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Search product in local and Open Food Fact databases
     *
     * @param searchedTerm wanted product name
     * @return found products
     */
    public List<ProductDTO> find(String searchedTerm) {
        if (searchedTerm == null) {
            throw new IllegalArgumentException("searched term must not be null");
        }
        List<Product> products = new ArrayList<>();

        if (searchedTerm.isEmpty()) {
            return ProductConverter.toDto(products);
        }

        List<Product> results = openFoodFactClient.search(searchedTerm);
        if (!CollectionUtils.isEmpty(results)) {
            log.debug("{} Open food fact results found for searched term: {}", results.size(), searchedTerm);
            products.addAll(results);
        }
        results = productService.find(searchedTerm);
        if (!CollectionUtils.isEmpty(results)) {
            log.debug("{} db results found for searched term: {}", results.size(), searchedTerm);
            products.addAll(results);
        }
        return ProductConverter.toDto(products);
    }


    /**
     * Search product by bar code
     * use Open Food Fact database
     *
     * @param barCode product bar code
     * @return found product
     */
    public ProductDTO searchByCode(String barCode) {
        log.debug("Search product with bar code: {}", barCode);
        return ProductConverter.toDto(openFoodFactClient.searchByCode(barCode));
    }

    @PostConstruct
    public void updateProducts() {

        List<Product> products = productService.findAll();
        products.forEach(product -> {
            try {
                if (ObjectUtils.isEmpty(product.getQrCode())) {
                    log.warn("cannot update product {}, barCode missing", product.getName());
                } else {
                    log.info("check for local product update: {} ({})", product.getName(), product.getQrCode());
                    Product found = openFoodFactClient.searchByCode(product.getQrCode());
                    if (!ObjectUtils.isEmpty(found)) {
                        updateNutrimentLevels(found, product);
                        updateNutrimentScore(found, product);
                        productService.save(product);
                        log.info("Product {} updated", found.getQrCode());
                    }
                }
            } catch (ResourceAccessException exception) {
                log.error("failed perform search request on Open Food Fact API. Error: {}", exception.getMessage());
            }
        });

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
