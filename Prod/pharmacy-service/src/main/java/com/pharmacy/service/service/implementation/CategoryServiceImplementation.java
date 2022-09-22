package com.pharmacy.service.service.implementation;

import com.pharmacy.service.dto.CategoryDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.Category;
import com.pharmacy.service.model.Product;
import com.pharmacy.service.repository.CategoryRepository;
import com.pharmacy.service.repository.ProductRepository;
import com.pharmacy.service.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public CategoryDto getCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty())
            throw new ResourceException("Category", "ID", categoryId, ResourceException.ErrorType.FOUND);
        Category category = optionalCategory.get();
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public PageableResponse<CategoryDto> getCategories(Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(requestedPage);
        List<Category> allCategories = categoryPage.getContent();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : allCategories)
            categoryDtoList.add(modelMapper.map(category, CategoryDto.class));
        PageableResponse<CategoryDto> pageableCategoryResponse = new PageableResponse<>();
        return pageableCategoryResponse.setResponseData(categoryDtoList, categoryPage);
    }

    @Override
    public List<CategoryDto> getCategories() {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryRepository.findAll().forEach(category ->  categoryDtoList.add(modelMapper.map(category, CategoryDto.class)));
        return categoryDtoList;
    }

    @Override
    public CategoryDto getCategoryDtoByName(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
        if (optionalCategory.isEmpty())
            throw new ResourceException("Category", "Name", categoryName, ResourceException.ErrorType.FOUND);
        return modelMapper.map(optionalCategory.get(), CategoryDto.class);
    }

    @Override
    public Optional<Category> getCategoryByNameOpt(String categoryName) {
        return categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
        if (optionalCategory.isEmpty())
            throw new ResourceException("Category", "Name", categoryName, ResourceException.ErrorType.FOUND);
        log.info(optionalCategory.stream().findFirst().toString());
        return optionalCategory.get();
    }

    @Override
    public Category getCategoryByNameOne(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
        if (optionalCategory.isEmpty())
            throw new ResourceException("Category", "Name", categoryName, ResourceException.ErrorType.FOUND);
        return optionalCategory.get();
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        try {
            return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
        } catch (Exception e) {
            throw new ResourceException("Category", "category", categoryDto, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public Category createCategoryByName(String categoryName) {
        try {
            return categoryRepository.save(new Category(categoryName));
        } catch (Exception e) {
            throw new ResourceException("Category", "Name", categoryName, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category newCategory = modelMapper.map(categoryDto, Category.class);
        Optional<Category> optionalCategory = categoryRepository.findById(newCategory.getCategoryId());
        if (optionalCategory.isEmpty())
            throw new ResourceException("Category", "category", categoryDto, ResourceException.ErrorType.FOUND);
        try {
            return modelMapper.map(categoryRepository.save(newCategory), CategoryDto.class);
        } catch (Exception e) {
            throw new ResourceException("Category", "category", categoryDto, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty())
            throw new ResourceException("Category", "ID", categoryId, ResourceException.ErrorType.FOUND);
        Category category = optionalCategory.get();
        List<Product> productList = new ArrayList<>();
        for(Product product: productRepository.findAllByCategorySetContains(category)) {
            Set<Category> categorySet = product.getCategorySet();
            categorySet.remove(category);
            product.setCategorySet(categorySet);
            productList.add(product);
        }
        try {
            productRepository.saveAll(productList);
        } catch (Exception e){
            throw new ResourceException("Products", "product list", productList, ResourceException.ErrorType.UPDATED, e);
        }
        try {
            categoryRepository.deleteById(categoryId);
        } catch (Exception e) {
            throw new ResourceException("Category", "ID", categoryId, ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public boolean checkIfExists(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }
}
