package com.pharmacy.service.service;

import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.ProductDto;
import com.pharmacy.service.model.Category;
import com.pharmacy.service.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public interface ProductService {
    ProductDto getProduct(Long productId);
    PageableResponse<ProductDto> getProducts(Integer pageNumber, Integer pageSize);
    ProductDto createProduct(ProductDto productDto);
    CompletableFuture<Boolean> createProducts(List<Product> productList);
    ProductDto updateProduct(ProductDto productDto);
    CompletableFuture<Boolean> updateProducts(List<Product> productList);
    void deleteProduct(Long productId);
    boolean checkIfExists(Long productId);
    void updateQuantity(Long productId, Long quantity);
    List<Product> findByCategory(Category category);
    void deleteByCategory(Category category);

}
