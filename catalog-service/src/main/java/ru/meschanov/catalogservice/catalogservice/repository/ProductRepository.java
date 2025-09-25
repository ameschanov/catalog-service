package ru.meschanov.catalogservice.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.meschanov.catalogservice.catalogservice.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
}
