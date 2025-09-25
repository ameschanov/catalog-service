package ru.meschanov.catalogservice.catalogservice.service.impl;

import com.sun.jdi.connect.VMStartException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.meschanov.catalogservice.catalogservice.dto.CategoryDto;
import ru.meschanov.catalogservice.catalogservice.dto.ProductDto;
import ru.meschanov.catalogservice.catalogservice.entity.Category;
import ru.meschanov.catalogservice.catalogservice.entity.Product;
import ru.meschanov.catalogservice.catalogservice.exception.NotFoundException;
import ru.meschanov.catalogservice.catalogservice.exception.ValidationException;
import ru.meschanov.catalogservice.catalogservice.repository.CategoryRepository;
import ru.meschanov.catalogservice.catalogservice.repository.ProductRepository;
import ru.meschanov.catalogservice.catalogservice.service.CatalogService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    @Cacheable(value = "categories")
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName(), c.getDescription()))
                .toList();
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDto createCategory(CategoryDto dto) {
        Category category = categoryRepository.save(Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build());
        return new CategoryDto(category.getId(), category.getName(), category.getDescription());
    }

    @Override
    @Cacheable(value = "products", key = "#categoryId")
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getCategory().getId()))
                .toList();
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductDto createProduct(ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Product product = productRepository.save(Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .category(category)
                .build());
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), category.getId());
    }

    @Override
    @Cacheable(value = "product", key = "#productId")
    public ProductDto getProductById(Long productId) {
        if (productId != null) {
            return productRepository.findById(productId)
                    .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getCategory().getId()))
                    .orElseThrow(() -> new NotFoundException("Product not found"));
        } else {
            throw new ValidationException("Product ID is null");
        }
    }
}
