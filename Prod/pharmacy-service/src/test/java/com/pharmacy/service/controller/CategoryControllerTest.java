package com.pharmacy.service.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacy.service.dto.CategoryDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.service.CategoryService;

import java.util.ArrayList;

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

@ContextConfiguration(classes = {CategoryController.class})
@ExtendWith(SpringExtension.class)
class CategoryControllerTest {
    @Autowired
    private CategoryController categoryController;

    @MockBean
    private CategoryService categoryService;

    /**
     * Method under test: {@link CategoryController#getCategories()}
     */
    @Test
    void testGetCategories() throws Exception {
        when(categoryService.getCategories()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/category/get-all-category");
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link CategoryController#getCategories()}
     */
    @Test
    void testGetCategories2() throws Exception {
        when(categoryService.getCategories()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/pharmacy/category/get-all-category");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link CategoryController#getCategories(Integer, Integer)}
     */
    @Test
    void testGetCategories3() throws Exception {
        when(categoryService.getCategories((Integer) any(), (Integer) any())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/category/get-category");
        MockMvcBuilders.standaloneSetup(categoryController)
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
     * Method under test: {@link CategoryController#createCategory(CategoryDto)}
     */
    @Test
    void testCreateCategory() throws Exception {
        when(categoryService.createCategory((CategoryDto) any())).thenReturn(new CategoryDto("Category Name"));

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(123L);
        categoryDto.setCategoryName("Category Name");
        String content = (new ObjectMapper()).writeValueAsString(categoryDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/pharmacy/category/create-category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"categoryId\":null,\"categoryName\":\"Category Name\"}"));
    }

    /**
     * Method under test: {@link CategoryController#updateCategory(CategoryDto)}
     */
    @Test
    void testUpdateCategory() throws Exception {
        when(categoryService.updateCategory((CategoryDto) any())).thenReturn(new CategoryDto("Category Name"));

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(123L);
        categoryDto.setCategoryName("Category Name");
        String content = (new ObjectMapper()).writeValueAsString(categoryDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/category/update-category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"categoryId\":null,\"categoryName\":\"Category Name\"}"));
    }

    /**
     * Method under test: {@link CategoryController#deleteCategoryById(Long)}
     */
    @Test
    void testDeleteCategoryById() throws Exception {
        doNothing().when(categoryService).deleteCategory((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/pharmacy/category/delete-category/{id}", 123L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Deleted successfully!"));
    }

    /**
     * Method under test: {@link CategoryController#deleteCategoryById(Long)}
     */
    @Test
    void testDeleteCategoryById2() throws Exception {
        doNothing().when(categoryService).deleteCategory((Long) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders
                .delete("/pharmacy/category/delete-category/{id}", 123L);
        deleteResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Deleted successfully!"));
    }

    /**
     * Method under test: {@link CategoryController#deleteCategoryByName(String)}
     */
    @Test
    void testDeleteCategoryByName() throws Exception {
        doNothing().when(categoryService).deleteCategory((Long) any());
        when(categoryService.getCategoryDtoByName((String) any())).thenReturn(new CategoryDto("Category Name"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/pharmacy/category/delete-category/name/{name}", "Name");
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Deleted!"));
    }

    /**
     * Method under test: {@link CategoryController#getCategoryById(Long)}
     */
    @Test
    void testGetCategoryById() throws Exception {
        when(categoryService.getCategory((Long) any())).thenReturn(new CategoryDto("Category Name"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/category/get-category/{id}",
                123L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"categoryId\":null,\"categoryName\":\"Category Name\"}"));
    }

    /**
     * Method under test: {@link CategoryController#getCategoryById(Long)}
     */
    @Test
    void testGetCategoryById2() throws Exception {
        when(categoryService.getCategories((Integer) any(), (Integer) any())).thenReturn(new PageableResponse<>());
        when(categoryService.getCategory((Long) any())).thenReturn(new CategoryDto("Category Name"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/category/get-category/{id}",
                "", "Uri Variables");
        MockMvcBuilders.standaloneSetup(categoryController)
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
     * Method under test: {@link CategoryController#getCategoryByName(String)}
     */
    @Test
    void testGetCategoryByName() throws Exception {
        when(categoryService.getCategory((Long) any())).thenReturn(new CategoryDto("Category Name"));
        when(categoryService.getCategoryDtoByName((String) any())).thenReturn(new CategoryDto("Category Name"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/category/get-category/name/{name}", "Name");
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"categoryId\":null,\"categoryName\":\"Category Name\"}"));
    }
}

