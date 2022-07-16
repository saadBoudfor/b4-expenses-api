package fr.b4.apps.common.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.b4.apps.common.dto.ProductCategoryDTO;
import fr.b4.apps.common.entities.ProductCategory;
import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.common.exceptions.ResourceNotSavedException;
import fr.b4.apps.common.services.ProductCategoryService;
import fr.b4.apps.common.util.converters.ProductCategoryConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products/categories")
public class ProductCategoryController {
    private final ProductCategoryService service;

    public ProductCategoryController(ProductCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductCategoryDTO> getAll() {
        return service.getAllCategories();
    }

    @PostMapping
    public ProductCategoryDTO save(@RequestParam(value = "file", required = false) MultipartFile file,
                           @RequestParam(value = "category") String categoryStr) throws BadRequestException {
        try {
            ProductCategory productCategory = ProductCategoryConverter.valueOf(categoryStr);
            return service.save(productCategory, file);
        } catch (JsonProcessingException | NullPointerException exception) {
            log.error("Error add new expense. Given expense is invalid");
            throw new BadRequestException("given category is invalid");
        } catch (IOException exception) {
            exception.printStackTrace();
            log.error("failed to save product category photo. Reason:  {}", exception.getMessage());
            throw new ResourceNotSavedException("failed to save product category photo");
        }
    }
}
