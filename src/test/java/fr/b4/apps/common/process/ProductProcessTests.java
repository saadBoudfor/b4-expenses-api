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

}
