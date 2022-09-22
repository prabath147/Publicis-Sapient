package com.admin.service.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.ProductDto;
import com.admin.service.service.AdminProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

@ContextConfiguration(classes = {AdminProductController.class})
@ExtendWith(SpringExtension.class)
class AdminProductControllerTest {
    @Autowired
    private AdminProductController adminProductController;

    @MockBean
    private AdminProductService adminProductService;

    @Test
    void testGetProductById() throws Exception {
        when(adminProductService.getProductById((Long) any())).thenReturn(new ProductDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/product/{id}", 123L);
        MockMvcBuilders.standaloneSetup(adminProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"productId\":null,\"proprietaryName\":null,\"productName\":null,\"description\":null,\"dosageForm\":null,"
                                        + "\"categorySet\":[],\"quantity\":0,\"imageUrl\":null,\"productType\":null}"));
    }

    @Test
    void testGetProductById2() throws Exception {
        when(adminProductService.getProducts((Integer) any(), (Integer) any())).thenReturn(new PageableResponse<>());
        when(adminProductService.getProductById((Long) any())).thenReturn(new ProductDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/product/{id}", "",
                "Uri Variables");
        MockMvcBuilders.standaloneSetup(adminProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage"
                                        + "\":null}"));
    }

    @Test
    void testCreateProduct() throws Exception {
        when(adminProductService.createProduct((ProductDto) any())).thenReturn(new ProductDto());

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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(adminProductController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"productId\":null,\"proprietaryName\":null,\"productName\":null,\"description\":null,\"dosageForm\":null,"
                                        + "\"categorySet\":[],\"quantity\":0,\"imageUrl\":null,\"productType\":null}"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(adminProductService.updateProduct((ProductDto) any())).thenReturn(new ProductDto());

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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/admin/product/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(adminProductController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"productId\":null,\"proprietaryName\":null,\"productName\":null,\"description\":null,\"dosageForm\":null,"
                                        + "\"categorySet\":[],\"quantity\":0,\"imageUrl\":null,\"productType\":null}"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        when(adminProductService.deleteProductById((Long) any())).thenReturn("42");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/product/{id}/delete", 123L);
        MockMvcBuilders.standaloneSetup(adminProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("42"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(adminProductService.getProducts((Integer) any(), (Integer) any())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/product/");
        MockMvcBuilders.standaloneSetup(adminProductController)
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