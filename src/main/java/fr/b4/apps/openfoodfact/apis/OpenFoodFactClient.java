package fr.b4.apps.openfoodfact.apis;

import fr.b4.apps.common.entities.NutrientLevels;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.common.exceptions.ThirdPartyException;
import fr.b4.apps.common.services.CategoryService;
import fr.b4.apps.common.util.converters.CategoryConverter;
import fr.b4.apps.openfoodfact.models.OFNutrientLevels;
import fr.b4.apps.openfoodfact.models.OFProduct;
import fr.b4.apps.openfoodfact.models.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OpenFoodFactClient {

    private final RestTemplate restTemplate;
    private final CategoryService categoryService;

    public OpenFoodFactClient(RestTemplate restTemplate, CategoryService categoryService) {
        this.restTemplate = restTemplate;
        this.categoryService = categoryService;
    }

    /**
     * Perform product search in Open Food Fact database
     *
     * @param search searched term
     * @return product list
     */
    public List<Product> search(String search) throws ThirdPartyException, ResourceNotFoundException {
        try {
            String url;
            if (ObjectUtils.isEmpty(search)) {
                url = "https://world.openfoodfacts.org/cgi/search.pl?&search_simple=1&action=process&json=1";
            } else {
                url = "https://world.openfoodfacts.org/cgi/search.pl?search_terms={search}&search_simple=1&action=process&json=1";
            }
            URI expanded = new UriTemplate(url).expand(search);
            url = URLDecoder.decode(expanded.toString(), StandardCharsets.UTF_8);
            ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
            if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getBody()) || ObjectUtils.isEmpty(response.getBody().getProducts())) {
                log.error("Failed to perform Open Food Fact search for searched term: {}", search);
                throw new ResourceNotFoundException("Open Food Fact respond with null body");
            }
            return response.getBody().getProducts().stream().map(OpenFoodFactClient::convert).collect(Collectors.toList());
        } catch (RestClientException exception) {
            log.error("Failed to perform Open Food Fact search for searched term: {}, error: {}", search, exception.getMessage());
            throw new ThirdPartyException("Failed to perform Open Food Fact search for searched term: " + search);
        }
    }

    // testing
    public Product searchByCode(String barCode) {
        if (StringUtils.hasLength(barCode)) {
            try {
                String url = "https://world.openfoodfacts.org/api/v2/search?code=" + barCode;
                ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
                if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getBody()) || ObjectUtils.isEmpty(response.getBody().getProducts())) {
                    log.error("Open Food Fact search: Invalid Response for barcode: {}", barCode);
                    throw new ResourceNotFoundException("Open Food Fact respond with null body");
                }
                return response.getBody().getProducts().stream().map(OpenFoodFactClient::convert).collect(Collectors.toList()).get(0);
            } catch (RestClientException exception) {
                log.error("Failed to perform Open Food Fact search for searched term: {}, error: {}", barCode, exception.getMessage());
                throw new ThirdPartyException("Failed to perform Open Food Fact search for searched term: " + barCode);
            }
        } else {
            log.error("Code bar is not valid or missing");
            throw new IllegalArgumentException("Code bar is not valid or missing");
        }
    }

    public void updateProductCategories() {
        Thread thread = new Thread(new CategoryUpdater(categoryService));
        thread.start();
    }

    private static Product convert(OFProduct openOFProduct) {
        if (ObjectUtils.isEmpty(openOFProduct)) {
            log.error("Failed to convert OpenFoodFact Object: Cannot convert null object");
            return null;
        }
        Product product = new Product();
        product.setName(openOFProduct.getProductName());
        product.setPhoto(openOFProduct.getImageFrontUrl());
        product.setQrCode(openOFProduct.getCode());

        if (!ObjectUtils.isEmpty(openOFProduct.getNutriments())) {
            product.setCalories(openOFProduct.getNutriments().getEnergyKcal());
        }

        product.setBrand(openOFProduct.getBrands());
        product.setDataPer(openOFProduct.getNutritionDataPer());
        if (!ObjectUtils.isEmpty(openOFProduct.getCategoriesTags())) {
            product.setCategories(Arrays.stream(openOFProduct.getCategoriesTags())
                    .map(CategoryConverter::convert)
                    .collect(Collectors.toList()));
        }

        if (!ObjectUtils.isEmpty(openOFProduct.getNutrientLevels())) {
            product.setNutrientLevels(convert(openOFProduct.getNutrientLevels()));
        }
        product.setScore(openOFProduct.getNutritionGrades());
        product.setUnit(extractUnit(openOFProduct));
        setQuantity(product, openOFProduct);

        return product;
    }

    private static void setQuantity(Product product, OFProduct ofProduct) {
        product.setProductQuantity(ofProduct.getProductQuantity());
        product.setServingQuantity(ofProduct.getServingQuantity());
        product.setServingSize(ofProduct.getServingSize());

        product.setDisplayQuantity(ofProduct.getQuantity());
    }


    private static NutrientLevels convert(OFNutrientLevels ofNutrientLevels) {
        NutrientLevels nutrientLevels = new NutrientLevels();
        nutrientLevels.setFat(ofNutrientLevels.getFat());
        nutrientLevels.setSalt(ofNutrientLevels.getSalt());
        nutrientLevels.setSaturatedFat(ofNutrientLevels.getSaturatedFat());
        nutrientLevels.setSugars(ofNutrientLevels.getSaturatedFat());
        return nutrientLevels;
    }

    private static String extractUnit(OFProduct ofProduct) {

        String[] gUnits = {"mg", "kg", "cg", "g"};
        boolean isMLUnit = true;

        if (!StringUtils.hasLength(ofProduct.getQuantity())) {
            return null;
        }

        for (String unit : gUnits) {
            if (ofProduct.getQuantity().toLowerCase().contains(unit)) {
                isMLUnit = false;
                break;
            }
        }

        return isMLUnit ? "ml" : "g";
    }

}
