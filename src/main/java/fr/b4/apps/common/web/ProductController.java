package fr.b4.apps.common.web;

import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.services.ProductService;
import fr.b4.apps.common.web.interfaces.IProductController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController implements IProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping("/{name}")
    public List<Product> find(@PathVariable("name") String name) {
        return productService.find(name);
    }
}
