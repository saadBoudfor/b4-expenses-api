package fr.b4.apps.common.web;

import fr.b4.apps.common.entities.Product;
import fr.b4.apps.common.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(value = "Product API", tags = "Products", protocols = "http")
@RestController
@RequestMapping("/products")
public class ProductController{
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @ApiOperation(value = "Add new product to database", response = Iterable.class, tags = "Products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product saved success", response = Product.class) })
    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    @ApiOperation(value = "Search Product by name. If name is not provided, return all products in system (please see Model section (in Product) to get accepted Product type values)", response = Iterable.class, tags = "Products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = Product[].class) })
    @GetMapping("/{name}")
    public List<Product> find(@PathVariable("name") String name) {
        return productService.find(name);
    }
}
