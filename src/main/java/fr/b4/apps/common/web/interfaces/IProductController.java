package fr.b4.apps.common.web.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.b4.apps.common.entities.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Api(value = "Product API",  tags = "Products", protocols = "http")
public interface IProductController {
    @ApiOperation(value = "Add new product to database", response = Product[].class, tags = "Products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product saved success", response = Product.class) })
    public Product save(MultipartFile file, String product) throws IOException;

    @ApiOperation(value = "Search Product by name. If name is not provided, return all products in system (please see Model section (in Product) to get accepted Product type values)", response = Product[].class, tags = "Products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = Product[].class) })
    public List<Product> find(String name);

}
