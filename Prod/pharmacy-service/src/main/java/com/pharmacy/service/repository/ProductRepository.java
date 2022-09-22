package com.pharmacy.service.repository;

import com.pharmacy.service.model.Category;
import com.pharmacy.service.model.Product;
import com.pharmacy.service.utils.ProductMini;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(Long productId);
    List<ProductMini> findAllByProductNameContainingOrProprietaryNameContaining(String productName, String proprietaryName);
    List<Product> findAllByCategorySetContains(Category category);
}

