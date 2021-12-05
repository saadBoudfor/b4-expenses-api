package fr.b4.apps.common.services;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.repositories.ProductRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTests {
    @Mock
    ProductRepository productRepository;

    @Test
    public void shouldSaveProductSuccess() {
        Product product = DataGenerator.generateProduct();

        ProductService productService = new ProductService(productRepository);
        productService.save(product);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void shouldSaveSuccessWithFile() throws IOException {
        Product product = DataGenerator.generateProduct();

        ProductService productService = new ProductService(productRepository);
        File file = new File("test.txt");
        file.createNewFile();
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", input.readAllBytes());
        productService.save(product, multipartFile);
        verify(productRepository, times(1)).save(product);
        file.delete();
    }

    @Test
    public void findAllSuccess() {
        List<Product> products = DataGenerator.generateProducts(6);
        when(productRepository.findAll()).thenReturn(products);

        ProductService productService = new ProductService(productRepository);
        List<Product> found = productService.findAll();

        verify(productRepository, times(1)).findAll();
        Assertions.assertEquals(found.size(), 6);
    }

    @Test
    public void findAllSuccessDTO() {
        List<Product> products = DataGenerator.generateProducts(6);
        when(productRepository.findAll()).thenReturn(products);

        ProductService productService = new ProductService(productRepository);
        List<ProductDTO> found = productService.findAllDTO();

        verify(productRepository, times(1)).findAll();
        Assertions.assertEquals(found.size(), 6);
    }

    @Test
    public void shouldFindProductSuccess() {
        List<Product> products = DataGenerator.generateProducts(6);
        when(productRepository.findByNameContains("test")).thenReturn(products);

        ProductService productService = new ProductService(productRepository);
        List<Product> found = productService.find("test");

        verify(productRepository, times(1)).findByNameContains("test");
        Assertions.assertEquals(found.size(), 6);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfSearchEmptyStr() {
        ProductService productService = new ProductService(productRepository);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> productService.find(""));
        Assertions.assertThrows(IllegalArgumentException.class, ()-> productService.find(null));
    }
}
