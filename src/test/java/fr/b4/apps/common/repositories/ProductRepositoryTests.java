package fr.b4.apps.common.repositories;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.common.entities.Product;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldFilterProductByName() {
        // given
        List<Product> products = DataGenerator.generateProducts(2);
        products.get(0).setName("kinder");
        products.get(1).setName("pistachio");
        productRepository.saveAll(products);

        // when
        List<Product> found = productRepository.findByNameContains("pis");

        // then
        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(products.get(1).getId(), found.get(0).getId());
    }

    @Test
    public void shouldGetOneProductByName() {
        // given
        List<Product> products = DataGenerator.generateProducts(2);
        products.get(0).setName("kinder");
        products.get(1).setName("pistachio");
        productRepository.saveAll(products);

        // when
        Product product = productRepository.findFirstByName("pistachio");
        // then
        Assertions.assertNotNull(product);
        Assertions.assertEquals(products.get(1).getId(), product.getId());
    }
}
