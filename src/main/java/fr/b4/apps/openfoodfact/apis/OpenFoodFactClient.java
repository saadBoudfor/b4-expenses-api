package fr.b4.apps.openfoodfact.apis;

import fr.b4.apps.common.entities.NutrientLevels;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.services.CategoryService;
import fr.b4.apps.common.util.converters.CategoryConverter;
import fr.b4.apps.openfoodfact.models.OFCategory;
import fr.b4.apps.openfoodfact.models.OFNutrientLevels;
import fr.b4.apps.openfoodfact.models.OFProduct;
import fr.b4.apps.openfoodfact.models.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OpenFoodFactClient {
    private RestTemplate restTemplate = new RestTemplate();
    private final CategoryService categoryService;

    public OpenFoodFactClient(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<Product> search(String search) {
        String url;
        restTemplate = new RestTemplate();
        if (ObjectUtils.isEmpty(search)) {
            url = "https://world.openfoodfacts.org/cgi/search.pl?&search_simple=1&action=process&json=1";
        } else {
            url = "https://world.openfoodfacts.org/cgi/search.pl?search_terms={search}&search_simple=1&action=process&json=1";
        }
        URI expanded = new UriTemplate(url).expand(search);
        url = URLDecoder.decode(expanded.toString(), StandardCharsets.UTF_8);
        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
        if (ObjectUtils.isEmpty(response.getBody()) || ObjectUtils.isEmpty(response.getBody().getProducts()))
            return new ArrayList<>();
        return response.getBody().getProducts().stream().map(OpenFoodFactClient::convert).collect(Collectors.toList());
    }

    public Product searchByCode(String barCode) {
        String url = "https://world.openfoodfacts.org/api/v2/search?code=" + barCode;
        restTemplate = new RestTemplate();
        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
        if (ObjectUtils.isEmpty(response.getBody()) || ObjectUtils.isEmpty(response.getBody().getProducts()))
            return null;
        return response.getBody().getProducts().stream().map(OpenFoodFactClient::convert).collect(Collectors.toList()).get(0);
    }

    public void updateProductCategories() {
        Thread thread = new Thread(new CategoryUpdater(categoryService));
        thread.start();
    }

    private static Product convert(OFProduct openOFProduct) {
        Product product = new Product();
        product.setName(openOFProduct.getProductName());
        product.setPhoto(openOFProduct.getImageFrontUrl());
        product.setQrCode(openOFProduct.getCode());
        if (StringUtils.hasLength(openOFProduct.getProductQuantity()))
            product.setQuantity(Float.valueOf(openOFProduct.getProductQuantity()));
        product.setDisplayQuantity(openOFProduct.getQuantity());
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
        return product;
    }

    private static NutrientLevels convert(OFNutrientLevels ofNutrientLevels) {
        NutrientLevels nutrientLevels = new NutrientLevels();
        nutrientLevels.setFat(ofNutrientLevels.getFat());
        nutrientLevels.setSalt(ofNutrientLevels.getSalt());
        nutrientLevels.setSaturatedFat(ofNutrientLevels.getSaturatedFat());
        nutrientLevels.setSugars(ofNutrientLevels.getSaturatedFat());
        return nutrientLevels;
    }

}
