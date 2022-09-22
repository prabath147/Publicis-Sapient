package com.admin.service.controller;

import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.ProductDto;
import com.admin.service.service.AdminProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/product")
@Slf4j
public class AdminProductController {

    @Autowired
    private AdminProductService adminProductService;

    @GetMapping("/")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    ) {

            PageableResponse<ProductDto> products = adminProductService.getProducts(pageNumber, pageSize);
            return ResponseEntity.status(HttpStatus.OK).body(products);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {

            ProductDto productDto = adminProductService.getProductById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(productDto);

    }

    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {

            productDto = adminProductService.createProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productDto);

    }

    @PutMapping("/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {

            productDto = adminProductService.updateProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productDto);

    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {

            String status = adminProductService.deleteProductById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(status);

    }
}
