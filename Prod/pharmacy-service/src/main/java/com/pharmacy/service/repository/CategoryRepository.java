package com.pharmacy.service.repository;

import com.pharmacy.service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryNameContainingIgnoreCase(String categoryName);

    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
}
