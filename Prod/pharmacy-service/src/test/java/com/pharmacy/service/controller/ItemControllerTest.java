package com.pharmacy.service.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacy.service.dto.ItemDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.ProductDto;
import com.pharmacy.service.dto.StoreDto;
import com.pharmacy.service.service.ItemService;
import com.pharmacy.service.service.ProductService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ItemController.class})
@ExtendWith(SpringExtension.class)
class ItemControllerTest {
    @Autowired
    private ItemController itemController;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ProductService productService;

    /**
     * Method under test: {@link ItemController#getItem(Long)}
     */
    @Test
    void testGetItem() throws Exception {
        when(itemService.getItem((Long) any())).thenReturn(new ItemDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/item/get-item/{id}", 123L);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"itemId\":null,\"store\":null,\"product\":null,\"itemQuantity\":null,\"price\":null,\"manufacturedDate\":null,"
                                        + "\"expiryDate\":null}"));
    }

    /**
     * Method under test: {@link ItemController#createItem(String, Long, ItemDto)}
     */
    @Test
    void testCreateItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        itemDto.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        itemDto.setItemId(123L);
        itemDto.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        itemDto.setManufacturedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        itemDto.setPrice(10.0d);
        itemDto.setProduct(new ProductDto());
        itemDto.setStore(new StoreDto());
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/pharmacy/item/create-item/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(itemController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ItemController#createItem(String, Long, ItemDto)}
     */
    @Test
    void testCreateItem2() throws Exception {
        when(itemService.createItem((String) any(), (Long) any(), (ItemDto) any())).thenReturn(new ItemDto());
        when(productService.getProduct((Long) any())).thenReturn(new ProductDto());
        when(productService.checkIfExists((Long) any())).thenReturn(true);
        when(productService.createProduct((ProductDto) any())).thenReturn(new ProductDto());
        doNothing().when(productService).updateQuantity((Long) any(), (Long) any());

        ItemDto itemDto = new ItemDto();
        itemDto.setExpiryDate(null);
        itemDto.setItemId(123L);
        itemDto.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        itemDto.setManufacturedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        itemDto.setPrice(10.0d);
        itemDto.setProduct(new ProductDto());
        itemDto.setStore(new StoreDto());
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/pharmacy/item/create-item/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"itemId\":null,\"store\":null,\"product\":null,\"itemQuantity\":null,\"price\":null,\"manufacturedDate\":null,"
                                        + "\"expiryDate\":null}"));
    }

    /**
     * Method under test: {@link ItemController#createItem(String, Long, ItemDto)}
     */
    @Test
    void testCreateItem3() throws Exception {
        when(itemService.createItem((String) any(), (Long) any(), (ItemDto) any())).thenReturn(new ItemDto());
        when(productService.getProduct((Long) any())).thenReturn(new ProductDto());
        when(productService.checkIfExists((Long) any())).thenReturn(false);
        when(productService.createProduct((ProductDto) any())).thenReturn(new ProductDto());
        doNothing().when(productService).updateQuantity((Long) any(), (Long) any());

        ItemDto itemDto = new ItemDto();
        itemDto.setExpiryDate(null);
        itemDto.setItemId(123L);
        itemDto.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        itemDto.setManufacturedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        itemDto.setPrice(10.0d);
        itemDto.setProduct(new ProductDto());
        itemDto.setStore(new StoreDto());
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/pharmacy/item/create-item/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"itemId\":null,\"store\":null,\"product\":null,\"itemQuantity\":null,\"price\":null,\"manufacturedDate\":null,"
                                        + "\"expiryDate\":null}"));
    }

    /**
     * Method under test: {@link ItemController#updateItem(String, ItemDto)}
     */
    @Test
    void testUpdateItem2() throws Exception {
        when(itemService.updateItem((String) any(), (ItemDto) any())).thenReturn(new ItemDto());

        ItemDto itemDto = new ItemDto();
        itemDto.setExpiryDate(null);
        itemDto.setItemId(123L);
        itemDto.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        itemDto.setManufacturedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        itemDto.setPrice(10.0d);
        itemDto.setProduct(new ProductDto());
        itemDto.setStore(new StoreDto());
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/item/update-item")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"itemId\":null,\"store\":null,\"product\":null,\"itemQuantity\":null,\"price\":null,\"manufacturedDate\":null,"
                                        + "\"expiryDate\":null}"));
    }

    /**
     * Method under test: {@link ItemController#deleteItem(String, Long)}
     */
    @Test
    void testDeleteItem() throws Exception {
        doNothing().when(itemService).deleteItem((String) any(), (Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/pharmacy/item/delete-item/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Deleted Item Successfully!"));
    }

    /**
     * Method under test: {@link ItemController#getItems(Integer, Integer)}
     */
    @Test
    void testGetItems() throws Exception {
        when(itemService.getItemsWithoutStoreInfo((Integer) any(), (Integer) any())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/item/get-item");
        MockMvcBuilders.standaloneSetup(itemController)
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
    void testGetTotalItems() throws Exception {
        when(itemService.getTotalItems()).thenReturn(5L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/item/total-items");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("5"));
    }

    /**
     * Method under test: {@link ItemController#getItemsByProductId(Integer, Integer, Long)}
     */
    @Test
    void testGetItemsByProductId() throws Exception {
        when(itemService.getSortedItemsByProductId((Integer) any(), (Integer) any(), (Long) any()))
                .thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/item/get-item-by-product-id/{id}", 123L);
        MockMvcBuilders.standaloneSetup(itemController)
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
     * Method under test: {@link ItemController#updateItem(String, ItemDto)}
     */
    @Test
    void testUpdateItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        itemDto.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        itemDto.setItemId(123L);
        itemDto.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        itemDto.setManufacturedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        itemDto.setPrice(10.0d);
        itemDto.setProduct(new ProductDto());
        itemDto.setStore(new StoreDto());
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/item/update-item")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

