package com.admin.service.service;


import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.ProductDto;
import org.springframework.stereotype.Component;


@Component
public interface AdminProductService {
    PageableResponse<ProductDto> getProducts(Integer pageNumber, Integer pageSize);

    ProductDto getProductById(Long productId);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    String deleteProductById(Long productId);

}
