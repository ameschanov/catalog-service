package ru.meschanov.catalogservice.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.meschanov.catalogservice.catalogservice.dto.CategoryDto;
import ru.meschanov.catalogservice.catalogservice.dto.ProductDto;
import ru.meschanov.catalogservice.catalogservice.service.CatalogService;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService service;

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories() {
        return service.getAllCategories();
    }

    @PostMapping("/categories")
    public CategoryDto createCategory(@RequestBody CategoryDto dto) {
        return service.createCategory(dto);
    }

    @GetMapping("/products")
    public List<ProductDto> getProductsByCategory(@RequestParam Long categoryId) {
        return service.getProductsByCategory(categoryId);
    }

    @PostMapping("/products")
    public ProductDto createProduct(@RequestBody ProductDto dto) {
        return service.createProduct(dto);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductById(id));
    }
}
