package com.admin.service.client.pharmacy;

import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "product",
        url = "${pharmacyServiceURL}/product"
)
public interface ProductClient {
    @PostMapping("/create-product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto);


    @GetMapping(value = "/get-product")
    public ResponseEntity<PageableResponse<ProductDto>> getProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize);


    @GetMapping(value = "/get-product/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId);


    @PutMapping(value = "/update-product")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto);


    @DeleteMapping(value = "/delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId);


}
