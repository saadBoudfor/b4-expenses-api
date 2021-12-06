package fr.b4.apps.common.process;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.NutrientLevelsRepository;
import fr.b4.apps.common.services.ProductService;
import fr.b4.apps.openfoodfact.apis.OpenFoodFactClient;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductProcessTests {

    @Mock
    ProductService productService;

    @Mock
    OpenFoodFactClient openFoodFactClient;

    @Mock
    NutrientLevelsRepository nutrientLevelsRepository;

    @DisplayName("Should return empty product list if no result was found")
    @Test
    public void shouldGetEmptyListIfNoResultFound() {
        when(productService.find("test")).thenReturn(new ArrayList<>());
        when(openFoodFactClient.search("test")).thenReturn(new ArrayList<>());

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);

        List<ProductDTO> found = productProcess.find("test");

        Assertions.assertTrue(found.isEmpty());
        verify(productService, times(1)).find("test");
        verify(openFoodFactClient, times(1)).search("test");
    }

    @DisplayName("Should return empty product list if given searched term is empty")
    @Test
    public void shouldGetEmptyListIfEmptyInput() {

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);

        List<ProductDTO> found = productProcess.find("");

        Assertions.assertTrue(found.isEmpty());
        verify(productService, never()).find("");
        verify(openFoodFactClient, never()).search("");
    }

    @DisplayName("Should throw illegal exception if input is invalid")
    @Test
    public void shouldThrowExceptionIfInvalidInput() {
        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);
        Assertions.assertThrows(IllegalArgumentException.class, () -> productProcess.find(null));
    }

    @DisplayName("Should return db products an open food fact results")
    @Test
    public void shouldReturnOFAndDBProductsSuccess() {
        when(productService.find("test")).thenReturn(DataGenerator.generateProducts(6));
        when(openFoodFactClient.search("test")).thenReturn(DataGenerator.generateProducts(4));

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);

        List<ProductDTO> found = productProcess.find("test");

        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(10, found.size());

        verify(productService, times(1)).find("test");
        verify(openFoodFactClient, times(1)).search("test");

    }

    @DisplayName("Should return only db products if no open food fact result")
    @Test
    public void shouldReturnOnlyDBProductIfNoOFResults() {
        when(productService.find("test")).thenReturn(new ArrayList<>());
        when(openFoodFactClient.search("test")).thenReturn(DataGenerator.generateProducts(4));

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);

        List<ProductDTO> found = productProcess.find("test");

        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(4, found.size());

        verify(productService, times(1)).find("test");
        verify(openFoodFactClient, times(1)).search("test");
    }

    @DisplayName("Should return only open food fact products if no db result")
    @Test
    public void shouldReturnOnlyOFProductIfNoDBResults() {
        when(productService.find("test")).thenReturn(DataGenerator.generateProducts(6));
        when(openFoodFactClient.search("test")).thenReturn(new ArrayList<>());

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);

        List<ProductDTO> found = productProcess.find("test");

        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(6, found.size());

        verify(productService, times(1)).find("test");
        verify(openFoodFactClient, times(1)).search("test");
    }

    @DisplayName("Should search product by bar code success")
    @Test
    public void shouldSearchProductByBarCodeSuccess() {
        when(openFoodFactClient.searchByCode("12345")).thenReturn(DataGenerator.generateProduct());

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);
        ProductDTO found = productProcess.searchByCode("12345");

        Assertions.assertFalse(ObjectUtils.isEmpty(found));
        verify(openFoodFactClient, times(1)).searchByCode("12345");
    }

    @Test
    public void shouldUpdateProductSuccess() {
        when(productService.findAll()).thenReturn(DataGenerator.generateProducts(6));
        when(openFoodFactClient.searchByCode(any())).thenReturn(DataGenerator.generateProduct());

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);
        productProcess.updateProducts();

        verify(openFoodFactClient, times(6)).searchByCode(any());
        verify(productService, times(6)).save(any());
        verify(nutrientLevelsRepository, never()).save(any());
    }

    @Test
    public void shouldUpdateProductWithNutrimentSuccess() {
        when(productService.findAll()).thenReturn(DataGenerator.generateProducts(6));
        Product found = DataGenerator.generateProduct();
        found.setNutrientLevels(DataGenerator.generateNutrientLevels());
        when(openFoodFactClient.searchByCode(any())).thenReturn(found);

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);
        productProcess.updateProducts();

        verify(openFoodFactClient, times(6)).searchByCode(any());
        verify(productService, times(6)).save(any());
        verify(nutrientLevelsRepository, times(6)).save(any());
    }

    @Test
    public void shouldNotUpdateProductsWithoutQrCode() {
        when(productService.findAll()).thenReturn(DataGenerator.generateProducts(6));
        when(openFoodFactClient.searchByCode(any())).thenReturn(null);

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);
        productProcess.updateProducts();

        verify(openFoodFactClient, times(6)).searchByCode(any());
        verify(productService, never()).save(any());
        verify(nutrientLevelsRepository, never()).save(any());
    }

    @Test
    public void shouldHandleOFClientHttpErrorWhenUpdateProducts() {
        List<Product> products = DataGenerator.generateProducts(6);
        when(productService.findAll()).thenReturn(products);

        when(openFoodFactClient.searchByCode(products.get(0).getQrCode()))
                .thenThrow(new ResourceAccessException("random error when call Open Food Fact Server"));
        for (int i = 1; i < 6; i++) {
            when(openFoodFactClient.searchByCode(products.get(i).getQrCode())).thenReturn(DataGenerator.generateProduct());
        }

        ProductProcess productProcess = new ProductProcess(productService, openFoodFactClient, nutrientLevelsRepository);
        productProcess.updateProducts();

        verify(openFoodFactClient, times(6)).searchByCode(any());
        verify(productService, times(5)).save(any());
    }

}
