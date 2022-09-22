package com.pharmacy.service.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pharmacy.service.dto.CategoryDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.ProductDto;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.Category;
import com.pharmacy.service.model.Product;
import com.pharmacy.service.repository.ProductRepository;
import com.pharmacy.service.service.CategoryService;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class ProductServiceImplementationTest {
    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImplementation productServiceImplementation;

    /**
     * Method under test: {@link ProductServiceImplementation#getProduct(Long)}
     */
    @Test
    void testGetProduct() {
        ProductDto productDto = new ProductDto();
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenReturn(productDto);

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
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(productDto, productServiceImplementation.getProduct(123L));
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#getProduct(Long)}
     */
    @Test
    void testGetProduct2() {
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

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
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> productServiceImplementation.getProduct(123L));
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#getProduct(Long)}
     */
    @Test
    void testGetProduct3() {
//        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
//        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenReturn(new ProductDto());
        when(productRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> productServiceImplementation.getProduct(123L));
//        verify(modelMapper).map((Object) any(), (Class<Object>) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#getProducts(Integer, Integer)}
     */
    @Test
    void testGetProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        when(productRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(productList));
        PageableResponse<ProductDto> actualProducts = productServiceImplementation.getProducts(10, 3);
        assertEquals(productList, actualProducts.getData());
        assertEquals(0L, actualProducts.getTotalRecords().longValue());
        assertEquals(1, actualProducts.getTotalPages().intValue());
        assertEquals(0, actualProducts.getPageSize().intValue());
        assertEquals(0, actualProducts.getPageNumber().intValue());
        assertTrue(actualProducts.getIsLastPage());
        verify(productRepository).findAll((Pageable) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#getProducts(Integer, Integer)}
     */
    @Test
    void testGetProducts2() {
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenReturn(new ProductDto());

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
        PageImpl<Product> pageImpl = new PageImpl<>(productList);
        when(productRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        PageableResponse<ProductDto> actualProducts = productServiceImplementation.getProducts(10, 3);
        assertEquals(1, actualProducts.getData().size());
        assertEquals(1L, actualProducts.getTotalRecords().longValue());
        assertEquals(1, actualProducts.getTotalPages().intValue());
        assertEquals(1, actualProducts.getPageSize().intValue());
        assertEquals(0, actualProducts.getPageNumber().intValue());
        assertTrue(actualProducts.getIsLastPage());
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
        verify(productRepository).findAll((Pageable) any());
    }

    @Test
    void testCreateProduct3() {
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
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product1);

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.createProduct(productDto));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(productRepository).save((Product) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#createProduct(ProductDto)}
     */
    @Test
    void testCreateProduct4() {
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
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);
        when(productRepository.save((Product) any()))
                .thenThrow(new ResourceException("Product", "Product", "Field Value", ResourceException.ErrorType.CREATED));

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.createProduct(productDto));
        verify(modelMapper).map((Object) any(), (Class<Product>) any());
        verify(productRepository).save((Product) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#createProduct(ProductDto)}
     */
    @Test
    void testCreateProduct5() {
        Product product = mock(Product.class);
        when(product.getCategorySet()).thenReturn(new HashSet<>());
        doNothing().when(product).setCategorySet((Set<Category>) any());
        doNothing().when(product).setDescription((String) any());
        doNothing().when(product).setDosageForm((String) any());
        doNothing().when(product).setImageUrl((String) any());
        doNothing().when(product).setProductId((Long) any());
        doNothing().when(product).setProductName((String) any());
        doNothing().when(product).setProductType(anyBoolean());
        doNothing().when(product).setProprietaryName((String) any());
        doNothing().when(product).setQuantity((Long) any());
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product1);

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.createProduct(productDto));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(product).getCategorySet();
        verify(product, atLeast(1)).setCategorySet((Set<Category>) any());
        verify(product).setDescription((String) any());
        verify(product).setDosageForm((String) any());
        verify(product).setImageUrl((String) any());
        verify(product).setProductId((Long) any());
        verify(product).setProductName((String) any());
        verify(product).setProductType(anyBoolean());
        verify(product).setProprietaryName((String) any());
        verify(product).setQuantity((Long) any());
        verify(productRepository).save((Product) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#createProduct(ProductDto)}
     */
    @Test
    void testCreateProduct6() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        when(categoryService.getCategoryByName((String) any())).thenReturn(category);

        Category category1 = new Category();
        category1.setCategoryId(123L);
        category1.setCategoryName("Product");

        HashSet<Category> categorySet = new HashSet<>();
        categorySet.add(category1);
        Product product = mock(Product.class);
        when(product.getCategorySet()).thenReturn(categorySet);
        doNothing().when(product).setCategorySet((Set<Category>) any());
        doNothing().when(product).setDescription((String) any());
        doNothing().when(product).setDosageForm((String) any());
        doNothing().when(product).setImageUrl((String) any());
        doNothing().when(product).setProductId((Long) any());
        doNothing().when(product).setProductName((String) any());
        doNothing().when(product).setProductType(anyBoolean());
        doNothing().when(product).setProprietaryName((String) any());
        doNothing().when(product).setQuantity((Long) any());
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product1);

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.createProduct(productDto));
        verify(categoryService).getCategoryByName((String) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(product).getCategorySet();
        verify(product, atLeast(1)).setCategorySet((Set<Category>) any());
        verify(product).setDescription((String) any());
        verify(product).setDosageForm((String) any());
        verify(product).setImageUrl((String) any());
        verify(product).setProductId((Long) any());
        verify(product).setProductName((String) any());
        verify(product).setProductType(anyBoolean());
        verify(product).setProprietaryName((String) any());
        verify(product).setQuantity((Long) any());
        verify(productRepository).save((Product) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#createProduct(ProductDto)}
     */
    @Test
    void testCreateProduct7() {
        when(categoryService.getCategoryByName((String) any()))
                .thenThrow(new ResourceException("Product", "Product", "Field Value", ResourceException.ErrorType.CREATED));

        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Product");

        HashSet<Category> categorySet = new HashSet<>();
        categorySet.add(category);
        Product product = mock(Product.class);
        when(product.getCategorySet()).thenReturn(categorySet);
        doNothing().when(product).setCategorySet((Set<Category>) any());
        doNothing().when(product).setDescription((String) any());
        doNothing().when(product).setDosageForm((String) any());
        doNothing().when(product).setImageUrl((String) any());
        doNothing().when(product).setProductId((Long) any());
        doNothing().when(product).setProductName((String) any());
        doNothing().when(product).setProductType(anyBoolean());
        doNothing().when(product).setProprietaryName((String) any());
        doNothing().when(product).setQuantity((Long) any());
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product1);

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.createProduct(productDto));
        verify(categoryService).getCategoryByName((String) any());
        verify(modelMapper).map((Object) any(), (Class<Product>) any());
        verify(product).getCategorySet();
        verify(product).setCategorySet((Set<Category>) any());
        verify(product).setDescription((String) any());
        verify(product).setDosageForm((String) any());
        verify(product).setImageUrl((String) any());
        verify(product).setProductId((Long) any());
        verify(product).setProductName((String) any());
        verify(product).setProductType(anyBoolean());
        verify(product).setProprietaryName((String) any());
        verify(product).setQuantity((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#createProducts(List)}
     */
    @Test
    void testCreateProducts() {
        when(productRepository.saveAll((Iterable<Product>) any())).thenReturn(new ArrayList<>());
        productServiceImplementation.createProducts(new ArrayList<>());
        verify(productRepository).saveAll((Iterable<Product>) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#createProducts(List)}
     */
    @Test
    void testCreateProducts2() {
        when(productRepository.saveAll((Iterable<Product>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> productServiceImplementation.createProducts(new ArrayList<>()));
        verify(productRepository).saveAll((Iterable<Product>) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#createProducts(List)}
     */
    @Test
    void testCreateProducts3() {
        when(productRepository.saveAll((Iterable<Product>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Product");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product");
        product.setProductType(true);
        product.setProprietaryName("Product");
        product.setQuantity(1L);

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product);
        assertThrows(ResourceException.class, () -> productServiceImplementation.createProducts(productList));
        verify(productRepository).saveAll((Iterable<Product>) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#createProducts(List)}
     */
    @Test
    void testCreateProducts4() {
        when(productRepository.saveAll((Iterable<Product>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Product");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product");
        product.setProductType(true);
        product.setProprietaryName("Product");
        product.setQuantity(1L);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Product");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product");
        product1.setProductType(true);
        product1.setProprietaryName("Product");
        product1.setQuantity(3L);

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product);
        assertThrows(ResourceException.class, () -> productServiceImplementation.createProducts(productList));
        verify(productRepository).saveAll((Iterable<Product>) any());
    }

    @Test
    void testUpdateProduct3() {
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
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        Optional<Product> ofResult = Optional.of(product1);

        Product product2 = new Product();
        product2.setCategorySet(new HashSet<>());
        product2.setDescription("The characteristics of someone or something");
        product2.setDosageForm("Dosage Form");
        product2.setImageUrl("https://example.org/example");
        product2.setProductId(123L);
        product2.setProductName("Product Name");
        product2.setProductType(true);
        product2.setProprietaryName("Proprietary Name");
        product2.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product2);
        when(productRepository.findById((Long) any())).thenReturn(ofResult);

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateProduct(productDto));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(productRepository).save((Product) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateProduct(ProductDto)}
     */
    @Test
    void testUpdateProduct4() {
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
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        Optional<Product> ofResult = Optional.of(product1);
        when(productRepository.save((Product) any()))
                .thenThrow(new ResourceException("Product", "Product", "Field Value", ResourceException.ErrorType.CREATED));
        when(productRepository.findById((Long) any())).thenReturn(ofResult);

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateProduct(productDto));
        verify(modelMapper).map((Object) any(), (Class<Product>) any());
        verify(productRepository).save((Product) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateProduct(ProductDto)}
     */
    @Test
    void testUpdateProduct5() {
        Product product = mock(Product.class);
        when(product.getProductId()).thenReturn(123L);
        when(product.getCategorySet()).thenReturn(new HashSet<>());
        doNothing().when(product).setCategorySet((Set<Category>) any());
        doNothing().when(product).setDescription((String) any());
        doNothing().when(product).setDosageForm((String) any());
        doNothing().when(product).setImageUrl((String) any());
        doNothing().when(product).setProductId((Long) any());
        doNothing().when(product).setProductName((String) any());
        doNothing().when(product).setProductType(anyBoolean());
        doNothing().when(product).setProprietaryName((String) any());
        doNothing().when(product).setQuantity((Long) any());
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        Optional<Product> ofResult = Optional.of(product1);

        Product product2 = new Product();
        product2.setCategorySet(new HashSet<>());
        product2.setDescription("The characteristics of someone or something");
        product2.setDosageForm("Dosage Form");
        product2.setImageUrl("https://example.org/example");
        product2.setProductId(123L);
        product2.setProductName("Product Name");
        product2.setProductType(true);
        product2.setProprietaryName("Proprietary Name");
        product2.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product2);
        when(productRepository.findById((Long) any())).thenReturn(ofResult);

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateProduct(productDto));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(product).getProductId();
        verify(product).getCategorySet();
        verify(product, atLeast(1)).setCategorySet((Set<Category>) any());
        verify(product).setDescription((String) any());
        verify(product).setDosageForm((String) any());
        verify(product).setImageUrl((String) any());
        verify(product).setProductId((Long) any());
        verify(product).setProductName((String) any());
        verify(product).setProductType(anyBoolean());
        verify(product).setProprietaryName((String) any());
        verify(product).setQuantity((Long) any());
        verify(productRepository).save((Product) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateProduct(ProductDto)}
     */
    @Test
    void testUpdateProduct6() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        when(categoryService.getCategoryByName((String) any())).thenReturn(category);

        Category category1 = new Category();
        category1.setCategoryId(123L);
        category1.setCategoryName("Product");

        HashSet<Category> categorySet = new HashSet<>();
        categorySet.add(category1);
        Product product = mock(Product.class);
        when(product.getProductId()).thenReturn(123L);
        when(product.getCategorySet()).thenReturn(categorySet);
        doNothing().when(product).setCategorySet((Set<Category>) any());
        doNothing().when(product).setDescription((String) any());
        doNothing().when(product).setDosageForm((String) any());
        doNothing().when(product).setImageUrl((String) any());
        doNothing().when(product).setProductId((Long) any());
        doNothing().when(product).setProductName((String) any());
        doNothing().when(product).setProductType(anyBoolean());
        doNothing().when(product).setProprietaryName((String) any());
        doNothing().when(product).setQuantity((Long) any());
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        Optional<Product> ofResult = Optional.of(product1);

        Product product2 = new Product();
        product2.setCategorySet(new HashSet<>());
        product2.setDescription("The characteristics of someone or something");
        product2.setDosageForm("Dosage Form");
        product2.setImageUrl("https://example.org/example");
        product2.setProductId(123L);
        product2.setProductName("Product Name");
        product2.setProductType(true);
        product2.setProprietaryName("Proprietary Name");
        product2.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product2);
        when(productRepository.findById((Long) any())).thenReturn(ofResult);

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateProduct(productDto));
        verify(categoryService).getCategoryByName((String) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(product).getProductId();
        verify(product).getCategorySet();
        verify(product, atLeast(1)).setCategorySet((Set<Category>) any());
        verify(product).setDescription((String) any());
        verify(product).setDosageForm((String) any());
        verify(product).setImageUrl((String) any());
        verify(product).setProductId((Long) any());
        verify(product).setProductName((String) any());
        verify(product).setProductType(anyBoolean());
        verify(product).setProprietaryName((String) any());
        verify(product).setQuantity((Long) any());
        verify(productRepository).save((Product) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateProduct(ProductDto)}
     */
    @Test
    void testUpdateProduct7() {
        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        when(categoryService.getCategoryByName((String) any())).thenReturn(category);

        Category category1 = new Category();
        category1.setCategoryId(123L);
        category1.setCategoryName("Product");

        HashSet<Category> categorySet = new HashSet<>();
        categorySet.add(category1);
        Product product = mock(Product.class);
        when(product.getProductId()).thenReturn(123L);
        when(product.getCategorySet()).thenReturn(categorySet);
        doNothing().when(product).setCategorySet((Set<Category>) any());
        doNothing().when(product).setDescription((String) any());
        doNothing().when(product).setDosageForm((String) any());
        doNothing().when(product).setImageUrl((String) any());
        doNothing().when(product).setProductId((Long) any());
        doNothing().when(product).setProductName((String) any());
        doNothing().when(product).setProductType(anyBoolean());
        doNothing().when(product).setProprietaryName((String) any());
        doNothing().when(product).setQuantity((Long) any());
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Product>) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product1);
        when(productRepository.findById((Long) any())).thenReturn(Optional.empty());

        ProductDto productDto = new ProductDto(123L, "Proprietary Name", "Product Name",
                "The characteristics of someone or something", "Dosage Form", new CategoryDto("Category Name"));
        productDto.setProductName("42");
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateProduct(productDto));
        verify(modelMapper).map((Object) any(), (Class<Product>) any());
        verify(product).getProductId();
        verify(product).setCategorySet((Set<Category>) any());
        verify(product).setDescription((String) any());
        verify(product).setDosageForm((String) any());
        verify(product).setImageUrl((String) any());
        verify(product).setProductId((Long) any());
        verify(product).setProductName((String) any());
        verify(product).setProductType(anyBoolean());
        verify(product).setProprietaryName((String) any());
        verify(product).setQuantity((Long) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateProducts(List)}
     */
    @Test
    void testUpdateProducts() {
        when(productRepository.saveAll((Iterable<Product>) any())).thenReturn(new ArrayList<>());
        productServiceImplementation.updateProducts(new ArrayList<>());
        verify(productRepository).saveAll((Iterable<Product>) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateProducts(List)}
     */
    @Test
    void testUpdateProducts2() {
        when(productRepository.saveAll((Iterable<Product>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateProducts(new ArrayList<>()));
        verify(productRepository).saveAll((Iterable<Product>) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateProducts(List)}
     */
    @Test
    void testUpdateProducts3() {
        when(productRepository.saveAll((Iterable<Product>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Product");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product");
        product.setProductType(true);
        product.setProprietaryName("Product");
        product.setQuantity(1L);

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product);
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateProducts(productList));
        verify(productRepository).saveAll((Iterable<Product>) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateProducts(List)}
     */
    @Test
    void testUpdateProducts4() {
        when(productRepository.saveAll((Iterable<Product>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Product");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product");
        product.setProductType(true);
        product.setProprietaryName("Product");
        product.setQuantity(1L);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Product");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product");
        product1.setProductType(true);
        product1.setProprietaryName("Product");
        product1.setQuantity(3L);

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product);
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateProducts(productList));
        verify(productRepository).saveAll((Iterable<Product>) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#deleteProduct(Long)}
     */
    @Test
    void testDeleteProduct() {
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
        Optional<Product> ofResult = Optional.of(product);
        doNothing().when(productRepository).deleteById((Long) any());
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        productServiceImplementation.deleteProduct(123L);
        verify(productRepository).findById((Long) any());
        verify(productRepository).deleteById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#deleteProduct(Long)}
     */
    @Test
    void testDeleteProduct2() {
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
        Optional<Product> ofResult = Optional.of(product);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(productRepository)
                .deleteById((Long) any());
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> productServiceImplementation.deleteProduct(123L));
        verify(productRepository).findById((Long) any());
        verify(productRepository).deleteById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#deleteProduct(Long)}
     */
    @Test
    void testDeleteProduct3() {
        doNothing().when(productRepository).deleteById((Long) any());
        when(productRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> productServiceImplementation.deleteProduct(123L));
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists() {
        when(productRepository.existsById((Long) any())).thenReturn(true);
        assertTrue(productServiceImplementation.checkIfExists(123L));
        verify(productRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists2() {
        when(productRepository.existsById((Long) any())).thenReturn(false);
        assertFalse(productServiceImplementation.checkIfExists(123L));
        verify(productRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists3() {
        when(productRepository.existsById((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> productServiceImplementation.checkIfExists(123L));
        verify(productRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateQuantity(Long, Long)}
     */
    @Test
    void testUpdateQuantity() {
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
        Optional<Product> ofResult = Optional.of(product);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product1);
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        productServiceImplementation.updateQuantity(123L, 1L);
        verify(productRepository).save((Product) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateQuantity(Long, Long)}
     */
    @Test
    void testUpdateQuantity2() {
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
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.save((Product) any()))
                .thenThrow(new ResourceException("Product", "Product", "Field Value", ResourceException.ErrorType.CREATED));
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateQuantity(123L, 1L));
        verify(productRepository).save((Product) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateQuantity(Long, Long)}
     */
    @Test
    void testUpdateQuantity3() {
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
        Optional<Product> ofResult = Optional.of(product);
        Product product1 = mock(Product.class);
        doNothing().when(product1).setCategorySet((Set<Category>) any());
        doNothing().when(product1).setDescription((String) any());
        doNothing().when(product1).setDosageForm((String) any());
        doNothing().when(product1).setImageUrl((String) any());
        doNothing().when(product1).setProductId((Long) any());
        doNothing().when(product1).setProductName((String) any());
        doNothing().when(product1).setProductType(anyBoolean());
        doNothing().when(product1).setProprietaryName((String) any());
        doNothing().when(product1).setQuantity((Long) any());
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product1);
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        productServiceImplementation.updateQuantity(123L, 1L);
        verify(productRepository).save((Product) any());
        verify(productRepository).findById((Long) any());
        verify(product1).setCategorySet((Set<Category>) any());
        verify(product1).setDescription((String) any());
        verify(product1).setDosageForm((String) any());
        verify(product1).setImageUrl((String) any());
        verify(product1).setProductId((Long) any());
        verify(product1).setProductName((String) any());
        verify(product1).setProductType(anyBoolean());
        verify(product1).setProprietaryName((String) any());
        verify(product1).setQuantity((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateQuantity(Long, Long)}
     */
    @Test
    void testUpdateQuantity4() {
        Product product = mock(Product.class);
        when(product.getQuantity()).thenReturn(1L);
        doNothing().when(product).setCategorySet((Set<Category>) any());
        doNothing().when(product).setDescription((String) any());
        doNothing().when(product).setDosageForm((String) any());
        doNothing().when(product).setImageUrl((String) any());
        doNothing().when(product).setProductId((Long) any());
        doNothing().when(product).setProductName((String) any());
        doNothing().when(product).setProductType(anyBoolean());
        doNothing().when(product).setProprietaryName((String) any());
        doNothing().when(product).setQuantity((Long) any());
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        Optional<Product> ofResult = Optional.of(product);
        Product product1 = mock(Product.class);
        doNothing().when(product1).setCategorySet((Set<Category>) any());
        doNothing().when(product1).setDescription((String) any());
        doNothing().when(product1).setDosageForm((String) any());
        doNothing().when(product1).setImageUrl((String) any());
        doNothing().when(product1).setProductId((Long) any());
        doNothing().when(product1).setProductName((String) any());
        doNothing().when(product1).setProductType(anyBoolean());
        doNothing().when(product1).setProprietaryName((String) any());
        doNothing().when(product1).setQuantity((Long) any());
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product1);
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        productServiceImplementation.updateQuantity(123L, 1L);
        verify(productRepository).save((Product) any());
        verify(productRepository).findById((Long) any());
        verify(product1).setCategorySet((Set<Category>) any());
        verify(product1).setDescription((String) any());
        verify(product1).setDosageForm((String) any());
        verify(product1).setImageUrl((String) any());
        verify(product1).setProductId((Long) any());
        verify(product1).setProductName((String) any());
        verify(product1).setProductType(anyBoolean());
        verify(product1).setProprietaryName((String) any());
        verify(product1).setQuantity((Long) any());
        verify(product).getQuantity();
        verify(product).setCategorySet((Set<Category>) any());
        verify(product).setDescription((String) any());
        verify(product).setDosageForm((String) any());
        verify(product).setImageUrl((String) any());
        verify(product).setProductId((Long) any());
        verify(product).setProductName((String) any());
        verify(product).setProductType(anyBoolean());
        verify(product).setProprietaryName((String) any());
        verify(product, atLeast(1)).setQuantity((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#updateQuantity(Long, Long)}
     */
    @Test
    void testUpdateQuantity5() {
        Product product = mock(Product.class);
        doNothing().when(product).setCategorySet((Set<Category>) any());
        doNothing().when(product).setDescription((String) any());
        doNothing().when(product).setDosageForm((String) any());
        doNothing().when(product).setImageUrl((String) any());
        doNothing().when(product).setProductId((Long) any());
        doNothing().when(product).setProductName((String) any());
        doNothing().when(product).setProductType(anyBoolean());
        doNothing().when(product).setProprietaryName((String) any());
        doNothing().when(product).setQuantity((Long) any());
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        when(productRepository.save((Product) any())).thenReturn(product);
        when(productRepository.findById((Long) any())).thenReturn(Optional.empty());
        Product product1 = mock(Product.class);
        when(product1.getQuantity()).thenReturn(1L);
        doNothing().when(product1).setCategorySet((Set<Category>) any());
        doNothing().when(product1).setDescription((String) any());
        doNothing().when(product1).setDosageForm((String) any());
        doNothing().when(product1).setImageUrl((String) any());
        doNothing().when(product1).setProductId((Long) any());
        doNothing().when(product1).setProductName((String) any());
        doNothing().when(product1).setProductType(anyBoolean());
        doNothing().when(product1).setProprietaryName((String) any());
        doNothing().when(product1).setQuantity((Long) any());
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        assertThrows(ResourceException.class, () -> productServiceImplementation.updateQuantity(123L, 1L));
        verify(productRepository).findById((Long) any());
        verify(product).setCategorySet((Set<Category>) any());
        verify(product).setDescription((String) any());
        verify(product).setDosageForm((String) any());
        verify(product).setImageUrl((String) any());
        verify(product).setProductId((Long) any());
        verify(product).setProductName((String) any());
        verify(product).setProductType(anyBoolean());
        verify(product).setProprietaryName((String) any());
        verify(product).setQuantity((Long) any());
        verify(product1).setCategorySet((Set<Category>) any());
        verify(product1).setDescription((String) any());
        verify(product1).setDosageForm((String) any());
        verify(product1).setImageUrl((String) any());
        verify(product1).setProductId((Long) any());
        verify(product1).setProductName((String) any());
        verify(product1).setProductType(anyBoolean());
        verify(product1).setProprietaryName((String) any());
        verify(product1).setQuantity((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#findByCategory(Category)}
     */
    @Test
    void testFindByCategory() {
        ArrayList<Product> productList = new ArrayList<>();
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(productList);

        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        List<Product> actualFindByCategoryResult = productServiceImplementation.findByCategory(category);
        assertSame(productList, actualFindByCategoryResult);
        assertTrue(actualFindByCategoryResult.isEmpty());
        verify(productRepository).findAllByCategorySetContains((Category) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#findByCategory(Category)}
     */
    @Test
    void testFindByCategory2() {
        when(productRepository.findAllByCategorySetContains((Category) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        assertThrows(ResourceException.class, () -> productServiceImplementation.findByCategory(category));
        verify(productRepository).findAllByCategorySetContains((Category) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#deleteByCategory(Category)}
     */
    @Test
    void testDeleteByCategory() {
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(new ArrayList<>());

        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        productServiceImplementation.deleteByCategory(category);
        verify(productRepository).findAllByCategorySetContains((Category) any());
        assertEquals(123L, category.getCategoryId().longValue());
        assertEquals("Category Name", category.getCategoryName());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#deleteByCategory(Category)}
     */
    @Test
    void testDeleteByCategory2() {
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

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        Optional<Product> ofResult = Optional.of(product1);
        doNothing().when(productRepository).deleteById((Long) any());
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(productList);

        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        productServiceImplementation.deleteByCategory(category);
        verify(productRepository).findAllByCategorySetContains((Category) any());
        verify(productRepository).findById((Long) any());
        verify(productRepository).deleteById((Long) any());
        assertEquals(123L, category.getCategoryId().longValue());
        assertEquals("Category Name", category.getCategoryName());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#deleteByCategory(Category)}
     */
    @Test
    void testDeleteByCategory3() {
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

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        Optional<Product> ofResult = Optional.of(product1);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(productRepository)
                .deleteById((Long) any());
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(productList);

        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        assertThrows(ResourceException.class, () -> productServiceImplementation.deleteByCategory(category));
        verify(productRepository).findAllByCategorySetContains((Category) any());
        verify(productRepository).findById((Long) any());
        verify(productRepository).deleteById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#deleteByCategory(Category)}
     */
    @Test
    void testDeleteByCategory4() {
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
        doNothing().when(productRepository).deleteById((Long) any());
        when(productRepository.findById((Long) any())).thenReturn(Optional.empty());
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(productList);

        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        assertThrows(ResourceException.class, () -> productServiceImplementation.deleteByCategory(category));
        verify(productRepository).findAllByCategorySetContains((Category) any());
        verify(productRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ProductServiceImplementation#deleteByCategory(Category)}
     */
    @Test
    void testDeleteByCategory5() {
        Product product = mock(Product.class);
        when(product.getProductId()).thenReturn(123L);
        when(product.getCategorySet()).thenReturn(new HashSet<>());
        doNothing().when(product).setCategorySet((Set<Category>) any());
        doNothing().when(product).setDescription((String) any());
        doNothing().when(product).setDosageForm((String) any());
        doNothing().when(product).setImageUrl((String) any());
        doNothing().when(product).setProductId((Long) any());
        doNothing().when(product).setProductName((String) any());
        doNothing().when(product).setProductType(anyBoolean());
        doNothing().when(product).setProprietaryName((String) any());
        doNothing().when(product).setQuantity((Long) any());
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

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        Optional<Product> ofResult = Optional.of(product1);
        doNothing().when(productRepository).deleteById((Long) any());
        when(productRepository.findById((Long) any())).thenReturn(ofResult);
        when(productRepository.findAllByCategorySetContains((Category) any())).thenReturn(productList);

        Category category = new Category();
        category.setCategoryId(123L);
        category.setCategoryName("Category Name");
        productServiceImplementation.deleteByCategory(category);
        verify(productRepository).findAllByCategorySetContains((Category) any());
        verify(productRepository).findById((Long) any());
        verify(productRepository).deleteById((Long) any());
        verify(product).getProductId();
        verify(product).getCategorySet();
        verify(product).setCategorySet((Set<Category>) any());
        verify(product).setDescription((String) any());
        verify(product).setDosageForm((String) any());
        verify(product).setImageUrl((String) any());
        verify(product).setProductId((Long) any());
        verify(product).setProductName((String) any());
        verify(product).setProductType(anyBoolean());
        verify(product).setProprietaryName((String) any());
        verify(product).setQuantity((Long) any());
    }
}

