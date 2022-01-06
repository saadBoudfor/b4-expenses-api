package fr.b4.apps.openfoodfact.apis;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.exceptions.ResourceNotFoundException;
import fr.b4.apps.common.exceptions.ThirdPartyException;
import fr.b4.apps.common.services.CategoryService;
import fr.b4.apps.openfoodfact.models.OFProduct;
import fr.b4.apps.openfoodfact.models.ProductResponse;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OpenFoodFactClientTests {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CategoryService categoryService;

    @Test
    public void shouldSearchProductSuccess() {
        ProductResponse productResponse = new ProductResponse();
        List<OFProduct> products = DataGenerator.generateOFProducts(9);
        productResponse.setProducts(products);
        when(restTemplate.getForEntity(anyString(), eq(ProductResponse.class))).thenReturn(ResponseEntity.accepted().body(productResponse));

        OpenFoodFactClient client = new OpenFoodFactClient(restTemplate, categoryService);
        List<Product> found = client.search("coca cola");

        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(found.size(), products.size());
    }

    @Test
    public void shouldReturnAllProductsSuccess() {
        ProductResponse productResponse = new ProductResponse();
        List<OFProduct> allProducts = DataGenerator.generateOFProducts(9);
        productResponse.setProducts(allProducts);
        when(restTemplate.getForEntity(anyString(), eq(ProductResponse.class))).thenReturn(ResponseEntity.accepted().body(productResponse));

        OpenFoodFactClient client = new OpenFoodFactClient(restTemplate, categoryService);
        List<Product> found = client.search("");

        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(found.size(), allProducts.size());
    }

    @Test
    public void shouldThrowExceptionIfOFReturnNullBodyForProductSearch() {
        OpenFoodFactClient client = new OpenFoodFactClient(restTemplate, categoryService);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> client.search("ok"));
    }

    @Test
    public void shouldThrowExceptionIfOFRespondWithErrorForProductSearch() {
        doThrow(new RestClientException("some error")).when(restTemplate).getForEntity(anyString(), eq(ProductResponse.class));
        OpenFoodFactClient client = new OpenFoodFactClient(restTemplate, categoryService);
        Assertions.assertThrows(ThirdPartyException.class, () -> client.search("ok"));
    }

    @Test
    public void shouldSearchProductByCodeSuccess() {
        ProductResponse productResponse = new ProductResponse();
        List<OFProduct> allProducts = DataGenerator.generateOFProducts(1);
        productResponse.setProducts(allProducts);
        when(restTemplate.getForEntity(anyString(), eq(ProductResponse.class))).thenReturn(ResponseEntity.accepted().body(productResponse));

        OpenFoodFactClient client = new OpenFoodFactClient(restTemplate, categoryService);
        Product found = client.searchByCode("7895423146");

        Assertions.assertFalse(ObjectUtils.isEmpty(found));
        Assertions.assertEquals(found.getName(), allProducts.get(0).getProductName());
    }

    @Test
    public void shouldThrowExceptionIfCodeInvalid() {
        OpenFoodFactClient client = new OpenFoodFactClient(restTemplate, categoryService);
        Assertions.assertThrows(IllegalArgumentException.class, () -> client.searchByCode(""));
    }


    @Test
    public void shouldThrowExceptionIfOFReturnNullBodyForProductSearchByCode() {
        OpenFoodFactClient client = new OpenFoodFactClient(restTemplate, categoryService);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> client.searchByCode("123456"));
    }

    @Test
    public void shouldThrowExceptionIfOFRespondWithErrorForProductSearchByCode() {
        doThrow(new RestClientException("some error")).when(restTemplate).getForEntity(anyString(), eq(ProductResponse.class));
        OpenFoodFactClient client = new OpenFoodFactClient(restTemplate, categoryService);
        Assertions.assertThrows(ThirdPartyException.class, () -> client.searchByCode("123456"));
    }
}
