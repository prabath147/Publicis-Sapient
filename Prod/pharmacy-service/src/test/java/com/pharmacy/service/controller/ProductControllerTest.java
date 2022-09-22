package com.pharmacy.service.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.ProductDto;
import com.pharmacy.service.service.ProductService;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ProductController.class})
@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    /**
     * Method under test: {@link ProductController#createProduct(ProductDto)}
     */
    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct((ProductDto) any())).thenReturn(new ProductDto());

        ProductDto productDto = new ProductDto();
        productDto.setCategorySet(new HashSet<>());
        productDto.setDescription("The characteristics of someone or something");
        productDto.setDosageForm("Dosage Form");
        productDto.setImageUrl("https://example.org/example");
        productDto.setProductId(123L);
        productDto.setProductName("Product Name");
        productDto.setProductType(true);
        productDto.setProprietaryName("Proprietary Name");
        productDto.setQuantity(1L);
        String content = (new ObjectMapper()).writeValueAsString(productDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/pharmacy/product/create-product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"productId\":null,\"proprietaryName\":null,\"productName\":null,\"description\":null,\"dosageForm\":null,"
                                        + "\"categorySet\":[],\"quantity\":0,\"imageUrl\":null,\"productType\":null}"));
    }

    /**
     * Method under test: {@link ProductController#getProducts(Integer, Integer)}
     */
    @Test
    void testGetProducts() throws Exception {
        when(productService.getProducts((Integer) any(), (Integer) any())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/product/get-product");
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage"
                                        + "\":null}"));
    }

    /**
     * Method under test: {@link ProductController#updateProduct(ProductDto)}
     */
    @Test
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct((ProductDto) any())).thenReturn(new ProductDto());

        ProductDto productDto = new ProductDto();
        productDto.setCategorySet(new HashSet<>());
        productDto.setDescription("The characteristics of someone or something");
        productDto.setDosageForm("Dosage Form");
        productDto.setImageUrl("https://example.org/example");
        productDto.setProductId(123L);
        productDto.setProductName("Product Name");
        productDto.setProductType(true);
        productDto.setProprietaryName("Proprietary Name");
        productDto.setQuantity(1L);
        String content = (new ObjectMapper()).writeValueAsString(productDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/product/update-product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"productId\":null,\"proprietaryName\":null,\"productName\":null,\"description\":null,\"dosageForm\":null,"
                                        + "\"categorySet\":[],\"quantity\":0,\"imageUrl\":null,\"productType\":null}"));
    }

    /**
     * Method under test: {@link ProductController#deleteProduct(Long)}
     */
    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/pharmacy/product/delete-product/{id}", 123L);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ProductController#deleteProduct(Long)}
     */
    @Test
    void testDeleteProduct2() throws Exception {
        doNothing().when(productService).deleteProduct((Long) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders
                .delete("/pharmacy/product/delete-product/{id}", 123L);
        deleteResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ProductController#getProductById(Long)}
     */
    @Test
    void testGetProductById() throws Exception {
        when(productService.getProduct((Long) any())).thenReturn(new ProductDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/product/get-product/{id}",
                123L);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"productId\":null,\"proprietaryName\":null,\"productName\":null,\"description\":null,\"dosageForm\":null,"
                                        + "\"categorySet\":[],\"quantity\":0,\"imageUrl\":null,\"productType\":null}"));
    }

    /**
     * Method under test: {@link ProductController#getProductById(Long)}
     */
    @Test
    void testGetProductById2() throws Exception {
        when(productService.getProducts((Integer) any(), (Integer) any())).thenReturn(new PageableResponse<>());
        when(productService.getProduct((Long) any())).thenReturn(new ProductDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/product/get-product/{id}",
                "", "Uri Variables");
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage"
                                        + "\":null}"));
    }
}

