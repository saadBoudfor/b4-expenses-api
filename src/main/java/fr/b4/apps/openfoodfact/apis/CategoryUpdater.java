package fr.b4.apps.openfoodfact.apis;

import fr.b4.apps.common.services.CategoryService;
import fr.b4.apps.openfoodfact.models.OFCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@Slf4j
public class CategoryUpdater implements Runnable{

    private final CategoryService categoryService;
    private RestTemplate restTemplate = new RestTemplate();

    public CategoryUpdater(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run() {
        final String url = "https://world.openfoodfacts.org/data/taxonomies/categories.json";
        ParameterizedTypeReference<LinkedHashMap<String, OFCategory>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<LinkedHashMap<String, OFCategory>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), typeRef);
        if (!CollectionUtils.isEmpty(response.getBody())) {
            response.getBody().forEach(categoryService::updateCategory);
            log.info("update all categories success");
        }
    }
}
