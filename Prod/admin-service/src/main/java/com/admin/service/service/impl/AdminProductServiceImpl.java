package com.admin.service.service.impl;

import com.admin.service.client.pharmacy.ProductClient;
import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.ProductDto;
import com.admin.service.exception.ResourceException;
import com.admin.service.service.AdminProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AdminProductServiceImpl implements AdminProductService {

    @Value("${pharmacyServiceURL}")
    private String pharmacyServiceURL;



    @Autowired
    private ProductClient productClient;

    @Override
    public PageableResponse<ProductDto> getProducts(Integer pageNumber, Integer pageSize) {

        ResponseEntity<PageableResponse<ProductDto>> response = productClient.getProducts(pageNumber, pageSize);
        return response.getBody();

    }

    @Override
    public ProductDto getProductById(Long productId) {
        try
        {
            ResponseEntity<ProductDto> response = productClient.getProductById(productId);
            return response.getBody();
        }
        catch (Exception e)
        {
            throw new ResourceException("Product", "productId", productId,ResourceException.ErrorType.FOUND);
        }

    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        try
        {
            ResponseEntity<ProductDto> response = productClient.createProduct(productDto);
            return response.getBody();
        }
        catch (Exception e)
        {
            throw new ResourceException("Product", "product", productDto, ResourceException.ErrorType.CREATED);
        }

    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        try
        {
            ResponseEntity<ProductDto> response = productClient.updateProduct(productDto);
            return response.getBody();
        }
        catch (Exception e)
        {
            throw new ResourceException("Product", "product", productDto, ResourceException.ErrorType.UPDATED);
        }
    }

    @Override
    public String deleteProductById(Long productId) {
        try
        {
            ResponseEntity<String> response = productClient.deleteProduct(productId);
            return response.getBody();
        }
        catch (Exception e)
        {
            throw new ResourceException("Product", "productId", productId,ResourceException.ErrorType.FOUND);
        }
    }

}
