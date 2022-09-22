package com.pharmacy.service.service.implementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.pharmacy.service.dto.AddressDto;
import com.pharmacy.service.dto.CategoryDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.Address;
import com.pharmacy.service.model.Category;
import com.pharmacy.service.model.Product;
import com.pharmacy.service.repository.CategoryRepository;
import com.pharmacy.service.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CategoryServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class CategoryServiceImplementationTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryServiceImplementation categoryServiceImplementation;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private ProductRepository productRepository;

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategory(Long)}
     */
    @Test
    void testGetCategory() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findById((Long) any())).thenReturn(ofResult);
        CategoryDto categoryDto = new CategoryDto("Category Name");
        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenReturn(categoryDto);
        assertSame(categoryDto, categoryServiceImplementation.getCategory(123L));
        verify(categoryRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CategoryDto>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategory(Long)}
     */
    @Test
    void testGetCategory2() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.getCategory(123L));
        verify(categoryRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CategoryDto>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategory(Long)}
     */
    @Test
    void testGetCategory3() {
        when(categoryRepository.findById((Long) any())).thenReturn(Optional.empty());
//        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
//        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenReturn(new CategoryDto("Category Name"));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.getCategory(123L));
        verify(categoryRepository).findById((Long) any());
//        verify(modelMapper).map((Object) any(), (Class<Object>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategories()}
     */
    @Test
    void testGetCategories() {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(categoryServiceImplementation.getCategories().isEmpty());
        verify(categoryRepository).findAll();
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategories()}
     */
    @Test
    void testGetCategories2() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenReturn(new CategoryDto("Category Name"));
        assertEquals(1, categoryServiceImplementation.getCategories().size());
        verify(categoryRepository).findAll();
        verify(modelMapper).map((Object) any(), (Class<CategoryDto>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategories()}
     */
    @Test
    void testGetCategories3() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");

        Category category1 = new Category();
        category1.setCategoryId(123L);
        category1.setCategoryName("Category Name");

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenReturn(new CategoryDto("Category Name"));
        assertEquals(2, categoryServiceImplementation.getCategories().size());
        verify(categoryRepository).findAll();
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<CategoryDto>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategories()}
     */
    @Test
    void testGetCategories4() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.getCategories());
        verify(categoryRepository).findAll();
        verify(modelMapper).map((Object) any(), (Class<CategoryDto>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategories(Integer, Integer)}
     */
    @Test
    void testGetCategories5() {
        ArrayList<Category> categoryList = new ArrayList<>();
        when(categoryRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(categoryList));
        PageableResponse<CategoryDto> actualCategories = categoryServiceImplementation.getCategories(10, 3);
        assertEquals(categoryList, actualCategories.getData());
        assertEquals(0L, actualCategories.getTotalRecords().longValue());
        assertEquals(1, actualCategories.getTotalPages().intValue());
        assertEquals(0, actualCategories.getPageSize().intValue());
        assertEquals(0, actualCategories.getPageNumber().intValue());
        assertTrue(actualCategories.getIsLastPage());
        verify(categoryRepository).findAll((Pageable) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategories(Integer, Integer)}
     */
    @Test
    void testGetCategories6() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        PageImpl<Category> pageImpl = new PageImpl<>(categoryList);
        when(categoryRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenReturn(new CategoryDto("Category Name"));
        PageableResponse<CategoryDto> actualCategories = categoryServiceImplementation.getCategories(10, 3);
        assertEquals(1, actualCategories.getData().size());
        assertEquals(1L, actualCategories.getTotalRecords().longValue());
        assertTrue(actualCategories.getIsLastPage());
        assertEquals(1, actualCategories.getTotalPages().intValue());
        assertEquals(1, actualCategories.getPageSize().intValue());
        assertEquals(0, actualCategories.getPageNumber().intValue());
        verify(categoryRepository).findAll((Pageable) any());
        verify(modelMapper).map((Object) any(), (Class<CategoryDto>) any());
    }

   

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategoryDtoByName(String)}
     */
    @Test
    void testGetCategoryDtoByName() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findByCategoryNameContainingIgnoreCase((String) any())).thenReturn(ofResult);
        CategoryDto categoryDto = new CategoryDto("Category Name");
        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenReturn(categoryDto);
        assertSame(categoryDto, categoryServiceImplementation.getCategoryDtoByName("Category Name"));
        verify(categoryRepository).findByCategoryNameContainingIgnoreCase((String) any());
        verify(modelMapper).map((Object) any(), (Class<CategoryDto>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategoryDtoByName(String)}
     */
    @Test
    void testGetCategoryDtoByName2() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findByCategoryNameContainingIgnoreCase((String) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.getCategoryDtoByName("Category Name"));
        verify(categoryRepository).findByCategoryNameContainingIgnoreCase((String) any());
        verify(modelMapper).map((Object) any(), (Class<CategoryDto>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategoryDtoByName(String)}
     */
    @Test
    void testGetCategoryDtoByName3() {
        when(categoryRepository.findByCategoryNameContainingIgnoreCase((String) any())).thenReturn(Optional.empty());
//        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
//        when(modelMapper.map((Object) any(), (Class<CategoryDto>) any())).thenReturn(new CategoryDto("Category Name"));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.getCategoryDtoByName("Category Name"));
        verify(categoryRepository).findByCategoryNameContainingIgnoreCase((String) any());
//        verify(modelMapper).map((Object) any(), (Class<Object>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategoryByNameOpt(String)}
     */
    @Test
    void testGetCategoryByNameOpt() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findByCategoryNameContainingIgnoreCase((String) any())).thenReturn(ofResult);
        Optional<Category> actualCategoryByNameOpt = categoryServiceImplementation.getCategoryByNameOpt("Category Name");
        assertSame(ofResult, actualCategoryByNameOpt);
        assertTrue(actualCategoryByNameOpt.isPresent());
        verify(categoryRepository).findByCategoryNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategoryByNameOpt(String)}
     */
    @Test
    void testGetCategoryByNameOpt2() {
        when(categoryRepository.findByCategoryNameContainingIgnoreCase((String) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.getCategoryByNameOpt("Category Name"));
        verify(categoryRepository).findByCategoryNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategoryByName(String)}
     */
    @Test
    void testGetCategoryByName() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findByCategoryNameContainingIgnoreCase((String) any())).thenReturn(ofResult);
        assertSame(category, categoryServiceImplementation.getCategoryByName("Category Name"));
        verify(categoryRepository).findByCategoryNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategoryByName(String)}
     */
    @Test
    void testGetCategoryByName2() {
        when(categoryRepository.findByCategoryNameContainingIgnoreCase((String) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.getCategoryByName("Category Name"));
        verify(categoryRepository).findByCategoryNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#getCategoryByName(String)}
     */
    @Test
    void testGetCategoryByName3() {
        when(categoryRepository.findByCategoryNameContainingIgnoreCase((String) any()))
                .thenThrow(new ResourceException("Category", "Category", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.getCategoryByName("Category Name"));
        verify(categoryRepository).findByCategoryNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#createCategory(CategoryDto)}
     */
    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        when(categoryRepository.save((Category) any())).thenReturn(category);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class,
                () -> categoryServiceImplementation.createCategory(new CategoryDto("Category Name")));
        verify(modelMapper).map((Object) any(), (Class<Category>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#createCategory(CategoryDto)}
     */
    @Test
    void testCreateCategory2() {
        Category category = mock(Category.class);
        doNothing().when(category).setCategoryId((Long) any());
        doNothing().when(category).setCategoryName((String) any());
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        when(categoryRepository.save((Category) any())).thenReturn(category);

        Category category1 = new Category();
        category1.setCategoryId(123L);
        category1.setCategoryName("Category Name");
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(category1);
        assertThrows(ResourceException.class,
                () -> categoryServiceImplementation.createCategory(new CategoryDto("Category Name")));
        verify(categoryRepository).save((Category) any());
        verify(category).setCategoryId((Long) any());
        verify(category).setCategoryName((String) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Category>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#createCategory(CategoryDto)}
     */
    @Test
    void testCreateCategory3() {
        Category category = mock(Category.class);
        doNothing().when(category).setCategoryId((Long) any());
        doNothing().when(category).setCategoryName((String) any());
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        when(categoryRepository.save((Category) any())).thenReturn(category);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(null);
        assertNull(categoryServiceImplementation.createCategory(new CategoryDto("Category Name")));
        verify(categoryRepository).save((Category) any());
        verify(category).setCategoryId((Long) any());
        verify(category).setCategoryName((String) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Category>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#createCategoryByName(String)}
     */
    @Test
    void testCreateCategoryByName() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        when(categoryRepository.save((Category) any())).thenReturn(category);
        assertSame(category, categoryServiceImplementation.createCategoryByName("Category Name"));
        verify(categoryRepository).save((Category) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#createCategoryByName(String)}
     */
    @Test
    void testCreateCategoryByName2() {
        when(categoryRepository.save((Category) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.createCategoryByName("Category Name"));
        verify(categoryRepository).save((Category) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#updateCategory(CategoryDto)}
     */
    @Test
    void testUpdateCategory() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);

        Category category1 = new Category();
        category1.setCategoryId(123L);
        category1.setCategoryName("Category Name");
        when(categoryRepository.save((Category) any())).thenReturn(category1);
        when(categoryRepository.findById((Long) any())).thenReturn(ofResult);

        Category category2 = new Category();
        category2.setCategoryId(123L);
        category2.setCategoryName("Category Name");
        when(modelMapper.map((Object) any(), (Class<Category>) any())).thenReturn(category2);
        assertThrows(ResourceException.class,
                () -> categoryServiceImplementation.updateCategory(new CategoryDto("Category Name")));
        verify(categoryRepository).save((Category) any());
        verify(categoryRepository).findById((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#updateCategory(CategoryDto)}
     */
    @Test
    void testUpdateCategory2() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.save((Category) any()))
                .thenThrow(new ResourceException("Category", "Category", "Field Value", ResourceException.ErrorType.CREATED));
        when(categoryRepository.findById((Long) any())).thenReturn(ofResult);

        Category category1 = new Category();
        category1.setCategoryId(123L);
        category1.setCategoryName("Category Name");
        when(modelMapper.map((Object) any(), (Class<Category>) any())).thenReturn(category1);
        assertThrows(ResourceException.class,
                () -> categoryServiceImplementation.updateCategory(new CategoryDto("Category Name")));
        verify(categoryRepository).save((Category) any());
        verify(categoryRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Category>) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#updateCategory(CategoryDto)}
     */
    @Test
    void testUpdateCategory3() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        when(categoryRepository.save((Category) any())).thenReturn(category);
        when(categoryRepository.findById((Long) any())).thenReturn(Optional.empty());

        Category category1 = new Category();
        category1.setCategoryId(123L);
        category1.setCategoryName("Category Name");
        when(modelMapper.map((Object) any(), (Class<Category>) any())).thenReturn(category1);
        assertThrows(ResourceException.class,
                () -> categoryServiceImplementation.updateCategory(new CategoryDto("Category Name")));
        verify(categoryRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Category>) any());
    }

//    @Test
//    void testUpdateCategory4() {
//        Category category = new Category();
//        category.setCategoryId(123L);
//        category.setCategoryName("Category Name");
//        CategoryDto category1 = modelMapper.map(category, CategoryDto.class);
//        Optional<Category> ofResult = Optional.of(category);
//        when(categoryRepository.save((Category)any())).thenReturn(category);
//        when(categoryRepository.findById((Long) any())).thenReturn(Optional.of(category));
//        assertNull(categoryServiceImplementation.updateCategory(category1));
//        verify(categoryRepository).findById((Long) any());
//        verify(categoryRepository).save((Category) any());
//        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
//
//    }


    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        doNothing().when(categoryRepository).deleteById((Long) any());
        when(categoryRepository.findById((Long) any())).thenReturn(ofResult);
        ArrayList<Product> productList = new ArrayList<>();
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(productList);
        when(productRepository.saveAll((Iterable<Product>) any())).thenReturn(new ArrayList<>());
        categoryServiceImplementation.deleteCategory(123L);
        verify(categoryRepository).findById((Long) any());
        verify(categoryRepository).deleteById((Long) any());
        verify(productRepository).findAllByCategorySetContains((Category) any());
        verify(productRepository).saveAll((Iterable<Product>) any());
        assertEquals(productList, categoryServiceImplementation.getCategories());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#deleteCategory(Long)}
     */
    @Test
    void testDeleteCategory2() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        doNothing().when(categoryRepository).deleteById((Long) any());
        when(categoryRepository.findById((Long) any())).thenReturn(ofResult);
        when(productRepository.findAllByCategorySetContains((Category) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(productRepository.saveAll((Iterable<Product>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.deleteCategory(123L));
        verify(categoryRepository).findById((Long) any());
        verify(productRepository).findAllByCategorySetContains((Category) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#deleteCategory(Long)}
     */
    @Test
    void testDeleteCategory3() {
        doNothing().when(categoryRepository).deleteById((Long) any());
        when(categoryRepository.findById((Long) any())).thenReturn(Optional.empty());
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(new ArrayList<>());
        when(productRepository.saveAll((Iterable<Product>) any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.deleteCategory(123L));
        verify(categoryRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#deleteCategory(Long)}
     */
    @Test
    void testDeleteCategory4() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        doNothing().when(categoryRepository).deleteById((Long) any());
        when(categoryRepository.findById((Long) any())).thenReturn(ofResult);

        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product);
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(productList);
        when(productRepository.saveAll((Iterable<Product>) any())).thenReturn(new ArrayList<>());
        categoryServiceImplementation.deleteCategory(123L);
        verify(categoryRepository).findById((Long) any());
        verify(categoryRepository).deleteById((Long) any());
        verify(productRepository).findAllByCategorySetContains((Category) any());
        verify(productRepository).saveAll((Iterable<Product>) any());
    }

    @Test
    void testDeleteCategory5() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        Optional<Category> ofResult = Optional.of(category);
        doNothing().when(categoryRepository).deleteById((Long) any());
        when(categoryRepository.findById((Long) any())).thenReturn(ofResult);

        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product);
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(productList);
        when(productRepository.saveAll((Iterable<Product>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.UPDATED));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.deleteCategory((Long) any()));
        verify(categoryRepository).findById((Long) any());
        verify(productRepository).findAllByCategorySetContains((Category) any());
    }
//    @Test
//    void testDeleteCategory6() {
//        Category category = new Category();
//        category.setCategoryId(123L);
//        category.setCategoryName("Category Name");
//        Optional<Category> ofResult = Optional.of(category);
//        doNothing().when(categoryRepository).deleteById((Long) any());
//        when(categoryRepository.findById((Long) any())).thenReturn(ofResult);
//
//        Product product = new Product();
//        product.setCategorySet(new HashSet<>());
//        product.setDescription("The characteristics of someone or something");
//        product.setDosageForm("Dosage Form");
//        product.setImageUrl("https://example.org/example");
//        product.setProductId(123L);
//        product.setProductName("Product Name");
//        product.setProductType(true);
//        product.setProprietaryName("Proprietary Name");
//        product.setQuantity(1L);
//
//        ArrayList<Product> productList = new ArrayList<>();
//        productList.add(product);
//        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(productList);
////        when(categoryRepository.deleteById(category.getCategoryId()));
//        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.DELETED));
////        when(categoryRepository.deleteById(1L)).thenThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.DELETED));
//        assertThrows(ResourceException.class, () -> categoryServiceImplementation.deleteCategory((Long) any()));
//        verify(categoryRepository).deleteById((Long) any());
////        verify(categoryRepository).findById((Long) any());
//        verify(productRepository).findAllByCategorySetContains((Category) any());
//    }


    /**
     * Method under test: {@link CategoryServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists() {
        when(categoryRepository.existsById((Long) any())).thenReturn(true);
        assertTrue(categoryServiceImplementation.checkIfExists(123L));
        verify(categoryRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists2() {
        when(categoryRepository.existsById((Long) any())).thenReturn(false);
        assertFalse(categoryServiceImplementation.checkIfExists(123L));
        verify(categoryRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link CategoryServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists3() {
        when(categoryRepository.existsById((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> categoryServiceImplementation.checkIfExists(123L));
        verify(categoryRepository).existsById((Long) any());
    }
}

