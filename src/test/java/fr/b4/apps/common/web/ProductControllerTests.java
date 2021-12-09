package fr.b4.apps.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.process.ProductProcess;
import fr.b4.apps.common.services.ProductService;
import fr.b4.apps.common.util.converters.ProductConverter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTests {
    @Mock
    ProductService productService;
    @Mock
    ProductProcess productProcess;

    @Test
    public void shouldSearchByTermSuccess() {
        List<ProductDTO> productDTOS = ProductConverter.toDto(DataGenerator.generateProducts(5));
        when(productProcess.find("coca")).thenReturn(productDTOS);

        ProductController productController = new ProductController(productService, productProcess);
        List<ProductDTO> found = productController.searchByTerm("coca");

        verify(productProcess, times(1)).find(any());
        Assertions.assertEquals(found, productDTOS);
    }

    @Test
    public void shouldGetAllProductFromDBSuccess() {
        List<ProductDTO> productDTOS = ProductConverter.toDto(DataGenerator.generateProducts(5));
        when(productService.findAllDTO()).thenReturn(productDTOS);

        ProductController productController = new ProductController(productService, productProcess);
        List<ProductDTO> found = productController.getAllFromDB();

        verify(productService, times(1)).findAllDTO();
        Assertions.assertEquals(found, productDTOS);
    }

    @Test
    public void searchByBarCodeSuccess() {
        ProductDTO productDTO = ProductConverter.toDto(DataGenerator.generateProduct());
        when(productProcess.searchByCode("0000")).thenReturn(productDTO);

        ProductController productController = new ProductController(productService, productProcess);
        ResponseEntity<Object> response = productController.searchByBarCode("0000");

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals((ProductDTO) response.getBody(), productDTO);
    }

    @Test
    public void shouldReturn404IfNotProductFound() {
        when(productProcess.searchByCode("0000")).thenReturn(null);

        ProductController productController = new ProductController(productService, productProcess);
        ResponseEntity<Object> response = productController.searchByBarCode("0000");

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldSaveProductSuccess() throws IOException {
        Product product = DataGenerator.generateProduct();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        when(productService.save(any(), any())).thenReturn(product);
        File file = new File("test.txt");
        file.createNewFile();
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", input.readAllBytes());

        ProductController productController = new ProductController(productService, productProcess);
        Product saved = productController.save(multipartFile,  ow.writeValueAsString(product));

        Assertions.assertEquals(saved, product);

    }
}
