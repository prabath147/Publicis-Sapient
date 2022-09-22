package com.admin.service.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.admin.service.client.pharmacy.ProductClient;
import com.admin.service.dto.ProductDto;
import com.admin.service.exception.ResourceException;
import com.admin.service.utils.ProductIC;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

@ContextConfiguration(classes = { AdminProductServiceImpl.class })
@ExtendWith(SpringExtension.class)
class AdminProductServiceImplTest {
        @Autowired
        private AdminProductServiceImpl adminProductServiceImpl;

        @MockBean
        private WebClient.Builder builder;

        @MockBean
        private ProductClient productClient;

        @Test
        void testGetProducts() {
                when(productClient.getProducts((Integer) any(), (Integer) any()))
                                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
                assertNull(adminProductServiceImpl.getProducts(10, 3));
                verify(productClient).getProducts((Integer) any(), (Integer) any());
        }

        @Test
        void testGetProducts3() {
                when(productClient.getProducts((Integer) any(), (Integer) any())).thenThrow(
                                new ResourceException("Resource Name", "Field Name", "Field Value",
                                                ResourceException.ErrorType.CREATED));
                assertThrows(ResourceException.class, () -> adminProductServiceImpl.getProducts(10, 3));
                verify(productClient).getProducts((Integer) any(), (Integer) any());
        }

        @Test
        void testGetProductById() {
                when(productClient.getProductById((Long) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
                assertNull(adminProductServiceImpl.getProductById(123L));
                verify(productClient).getProductById((Long) any());
        }

        @Test
        void testGetProductById2() {
                when(productClient.getProductById((Long) any())).thenReturn(null);
                assertThrows(ResourceException.class, () -> adminProductServiceImpl.getProductById(123L));
                verify(productClient).getProductById((Long) any());
        }

        @Test
        void testGetProductById3() {
                ResponseEntity<ProductDto> responseEntity = (ResponseEntity<ProductDto>) mock(ResponseEntity.class);
                when(responseEntity.getBody()).thenThrow(
                                new ResourceException("Resource Name", "Field Name", "Field Value",
                                                ResourceException.ErrorType.CREATED));
                when(productClient.getProductById((Long) any())).thenReturn(responseEntity);
                assertThrows(ResourceException.class, () -> adminProductServiceImpl.getProductById(123L));
                verify(productClient).getProductById((Long) any());
                verify(responseEntity).getBody();
        }

        @Test
        void testCreateProduct() {
                when(productClient.createProduct((ProductDto) any()))
                                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
                assertNull(adminProductServiceImpl.createProduct(new ProductDto()));
                verify(productClient).createProduct((ProductDto) any());
        }

        @Test
        void testCreateProduct2() {
                when(productClient.createProduct((ProductDto) any())).thenReturn(null);
                assertThrows(ResourceException.class, () -> adminProductServiceImpl.createProduct(new ProductDto()));
                verify(productClient).createProduct((ProductDto) any());
        }

        @Test
        void testCreateProduct3() {
                ResponseEntity<ProductDto> responseEntity = (ResponseEntity<ProductDto>) mock(ResponseEntity.class);
                when(responseEntity.getBody()).thenThrow(
                                new ResourceException("Resource Name", "Field Name", "Field Value",
                                                ResourceException.ErrorType.CREATED));
                when(productClient.createProduct((ProductDto) any())).thenReturn(responseEntity);
                assertThrows(ResourceException.class, () -> adminProductServiceImpl.createProduct(new ProductDto()));
                verify(productClient).createProduct((ProductDto) any());
                verify(responseEntity).getBody();
        }

        @Test
        void testUpdateProduct() {
                when(productClient.updateProduct((ProductDto) any()))
                                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
                assertNull(adminProductServiceImpl.updateProduct(new ProductDto()));
                verify(productClient).updateProduct((ProductDto) any());
        }

        @Test
        void testUpdateProduct2() {
                when(productClient.updateProduct((ProductDto) any())).thenReturn(null);
                assertThrows(ResourceException.class, () -> adminProductServiceImpl.updateProduct(new ProductDto()));
                verify(productClient).updateProduct((ProductDto) any());
        }

        @Test
        void testUpdateProduct3() {
                ResponseEntity<ProductDto> responseEntity = (ResponseEntity<ProductDto>) mock(ResponseEntity.class);
                when(responseEntity.getBody()).thenThrow(
                                new ResourceException("Resource Name", "Field Name", "Field Value",
                                                ResourceException.ErrorType.CREATED));
                when(productClient.updateProduct((ProductDto) any())).thenReturn(responseEntity);
                assertThrows(ResourceException.class, () -> adminProductServiceImpl.updateProduct(new ProductDto()));
                verify(productClient).updateProduct((ProductDto) any());
                verify(responseEntity).getBody();
        }

        @Test
        void testDeleteProductById() {
                when(productClient.deleteProduct((Long) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
                assertNull(adminProductServiceImpl.deleteProductById(123L));
                verify(productClient).deleteProduct((Long) any());
        }

        @Test
        void testDeleteProductById2() {
                when(productClient.deleteProduct((Long) any())).thenReturn(null);
                assertThrows(ResourceException.class, () -> adminProductServiceImpl.deleteProductById(123L));
                verify(productClient).deleteProduct((Long) any());
        }

        @Test
        void testDeleteProductById3() {
                ResponseEntity<String> responseEntity = (ResponseEntity<String>) mock(ResponseEntity.class);
                when(responseEntity.getBody()).thenThrow(
                                new ResourceException("Resource Name", "Field Name", "Field Value",
                                                ResourceException.ErrorType.CREATED));
                when(productClient.deleteProduct((Long) any())).thenReturn(responseEntity);
                assertThrows(ResourceException.class, () -> adminProductServiceImpl.deleteProductById(123L));
                verify(productClient).deleteProduct((Long) any());
                verify(responseEntity).getBody();
        }

}
