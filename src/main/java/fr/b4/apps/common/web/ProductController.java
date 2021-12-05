package fr.b4.apps.common.web;

import fr.b4.apps.common.process.ProductProcess;
import fr.b4.apps.common.dto.ProductDTO;
import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.services.ProductService;
import fr.b4.apps.common.util.converters.ProductConverter;
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
public class ProductController {
    private final ProductService productService;
    private final ProductProcess productProcess;

    public ProductController(ProductService productService,
                             ProductProcess productProcess) {
        this.productService = productService;
        this.productProcess = productProcess;
    }

    @GetMapping("/{name}")
    public List<ProductDTO> searchByTerm(@PathVariable(value = "name", required = true) String name) {
        return productProcess.find(name);
    }

    @GetMapping("/code/{code-bar}")
    public ResponseEntity<Object> searchByBarCode(@PathVariable(value = "code-bar", required = true) String codeBar) {
        ProductDTO found = productProcess.searchByCode(codeBar);
        if (ObjectUtils.isEmpty(found))
            return ResponseEntity.status(404).body(new MessageDTO("Product unknonwn"));
        return ResponseEntity.ok(found);
    }

    @GetMapping("/last")
    public List<ProductDTO> getAllFromDB() {
        return productService.findAllDTO();
    }


    @PostMapping
    public Product save(@RequestParam(value = "file", required = false) MultipartFile file,
                        @RequestParam(value = "product", required = true) String data
    ) throws IOException {
        Product product = ProductConverter.valueOf(data);
        return productService.save(product, file);
    }
}

