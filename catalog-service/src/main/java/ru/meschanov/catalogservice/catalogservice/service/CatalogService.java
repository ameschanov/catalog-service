package ru.meschanov.catalogservice.catalogservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import ru.meschanov.catalogservice.catalogservice.dto.CategoryDto;
import ru.meschanov.catalogservice.catalogservice.dto.ProductDto;

import java.util.List;

public interface CatalogService {
    @Cacheable(value = "categories")
    List<CategoryDto> getAllCategories();

    @CacheEvict(value = "categories", allEntries = true)
    CategoryDto createCategory(CategoryDto dto);

    @Cacheable(value = "products", key = "#categoryId")
    List<ProductDto> getProductsByCategory(Long categoryId);

    @CacheEvict(value = "products", allEntries = true)
    ProductDto createProduct(ProductDto dto);

    ProductDto getProductById(Long productId);
}
