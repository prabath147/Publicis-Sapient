package com.pharmacy.service.service;

import com.pharmacy.service.dto.CategoryDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface CategoryService {
    CategoryDto getCategory(Long categoryId);
    PageableResponse<CategoryDto> getCategories(Integer pageNumber, Integer pageSize);
    List<CategoryDto> getCategories();
    CategoryDto getCategoryDtoByName(String categoryName);
    Optional<Category> getCategoryByNameOpt(String categoryName);
    Category getCategoryByName(String categoryName);
    Category getCategoryByNameOne(String categoryName);
    CategoryDto createCategory(CategoryDto categoryDto);
    Category createCategoryByName(String categoryName);
    CategoryDto updateCategory(CategoryDto categoryDto);
    void deleteCategory(Long categoryId);
    boolean checkIfExists(Long categoryId);
}
