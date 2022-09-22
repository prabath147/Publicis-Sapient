package com.pharmacy.service.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.pharmacy.service.dto.ItemDto;
import com.pharmacy.service.dto.ItemProductDto;
import com.pharmacy.service.service.implementation.ItemProductServiceImplementation;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ItemProductController.class})
@ExtendWith(SpringExtension.class)
class ItemProductControllerTest {
    @Autowired
    private ItemProductController itemProductController;

    @MockBean
    private ItemProductServiceImplementation itemProductServiceImplementation;

    /**
     * Method under test: {@link ItemProductController#exactMatchSearchByStore(Long, String, int, int)}
     */
    @Test
    void testExactMatchSearchByStore() throws Exception {
        when(itemProductServiceImplementation.exactSearchByStore((Long) any(), (String) any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/search/search-items-exact-match/{storeId}", 123L);
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link ItemProductController#exactMatchSearchByStore(Long, String, int, int)}
     */
    @Test
    void testExactMatchSearchByStore2() throws Exception {
        when(itemProductServiceImplementation.exactSearch((String) any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(itemProductServiceImplementation.exactSearchByStore((Long) any(), (String) any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/search/search-items-exact-match/{storeId}", "", "Uri Variables");
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link ItemProductController#search(String, boolean, String, int, int)}
     */
    @Test
    void testSearch() throws Exception {
        when(itemProductServiceImplementation.search((String) any(), anyBoolean(), (String) any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/search/items");
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .json(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

    /**
     * Method under test: {@link ItemProductController#search(String, boolean, String, int, int)}
     */
    @Test
    void testSearch2() throws Exception {
        when(itemProductServiceImplementation.search((String) any(), anyBoolean(), (String) any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/pharmacy/search/items");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("productType", String.valueOf(true));
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .json(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

    /**
     * Method under test: {@link ItemProductController#exactMatchSearch(String, int, int)}
     */
    @Test
    void testExactMatchSearch() throws Exception {
        when(itemProductServiceImplementation.exactSearch((String) any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/search/search-items-exact-match");
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .json(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

    /**
     * Method under test: {@link ItemProductController#getItemsByStoreId(Long, int, int)}
     */
    @Test
    void testGetItemsByStoreId() throws Exception {
        when(itemProductServiceImplementation.getItemsByStoreId((Long) any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/search/item-products-by-store-id/{id}", 123L);
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .json(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"number\":0,\"size\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty\":true}"));
    }

    /**
     * Method under test: {@link ItemProductController#getSuggestions(String)}
     */
    @Test
    void testGetSuggestions() throws Exception {
        when(itemProductServiceImplementation.getSuggestions((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/search/get-suggestions");
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ItemProductController#getSuggestions(String)}
     */
    @Test
    void testGetSuggestions2() throws Exception {
        when(itemProductServiceImplementation.getSuggestions((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/pharmacy/search/get-suggestions");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ItemProductController#findAllItemProducts(int, int, String, boolean)}
     */
    @Test
    void testFindAllItemProducts() throws Exception {
        when(itemProductServiceImplementation.findAll(anyBoolean(), (String) any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/search/all-items");
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .json(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

    /**
     * Method under test: {@link ItemProductController#findAllItemProducts(int, int, String, boolean)}
     */
    @Test
    void testFindAllItemProducts2() throws Exception {
        when(itemProductServiceImplementation.findAll(anyBoolean(), (String) any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/pharmacy/search/all-items");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("productType", String.valueOf(true));
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .json(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

    /**
     * Method under test: {@link ItemProductController#getItemById(String)}
     */
    @Test
    void testGetItemById() throws Exception {
        when(itemProductServiceImplementation.findById((String) any())).thenReturn(new ItemProductDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/search/get-item-product/{id}", "42");
        MockMvcBuilders.standaloneSetup(itemProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"itemId\":null,\"productId\":null,\"storeId\":null,\"productName\":null,\"proprietaryName\""
                                        + ":null,\"price\":null,\"dosageForm\":null,\"productType\":false,\"imageUrl\":null,\"quantity\":null,\"description"
                                        + "\":null}"));
    }
}

