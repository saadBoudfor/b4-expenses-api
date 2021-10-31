package fr.b4.apps.common.web;

import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.services.ProductService;
import fr.b4.apps.common.util.converters.ProductConverter;
import fr.b4.apps.common.web.interfaces.IProductController;
import fr.b4.apps.expenses.dto.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController implements IProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{name}")
    public List<Product> find(@PathVariable(value = "name", required = true) String name) {
        return productService.find(name);
    }

    @GetMapping("/code/{code-bar}")
    public ResponseEntity<Object> searchByCode(@PathVariable(value = "code-bar", required = true) String codeBar) {
        Product found = productService.searchByCode(codeBar);
        if (ObjectUtils.isEmpty(found))
            return ResponseEntity.status(404).body(new MessageDTO("Product unknonwn"));
        return ResponseEntity.ok(found);
    }

    @GetMapping
    public List<Product> find() {
        return productService.find(null);
    }

    @PostMapping
    public Product save(@RequestParam(value = "file", required = false) MultipartFile file,
                        @RequestParam(value = "product", required = true) String data
    ) throws IOException {
        Product product = ProductConverter.valueOf(data);
        return productService.save(product, file);
    }
}
