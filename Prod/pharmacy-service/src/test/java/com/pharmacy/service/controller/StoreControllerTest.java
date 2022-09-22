package com.pharmacy.service.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacy.service.client.notify.NotificationClient;
import com.pharmacy.service.dto.AddressDto;
import com.pharmacy.service.dto.ItemDto;
import com.pharmacy.service.dto.ManagerDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.StoreDto;
import com.pharmacy.service.dtoexternal.CartDto;
import com.pharmacy.service.model.ApprovalStatus;
import com.pharmacy.service.service.CategoryService;
import com.pharmacy.service.service.ItemService;
import com.pharmacy.service.service.ManagerService;
import com.pharmacy.service.service.ProductService;
import com.pharmacy.service.service.StoreService;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.pharmacy.service.utils.JwtUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {StoreController.class})
@ExtendWith(SpringExtension.class)
class StoreControllerTest {
    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ManagerService managerService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private NotificationClient notificationClient;

    @MockBean
    private ProductService productService;


    @Autowired
    private StoreController storeController;

    @MockBean
    private StoreService storeService;

    StoreDto mockStoreDto;
    private static final String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjEiLCJpYXQiOjE2NjI5NTY5MzQsImV4cCI6MTY2Mjk2MDUzNCwicm9sZSI6IlJPTEVfQURNSU4iLCJpZCI6Mn0.OmKp6M8jw3um6E-OpSrMTu-JmYg0pj00lMJMxGBoM2JYsqmqIVOOVX-fpm-yQ_d7nRw33s1j8V9ggAMDH9xSaw";

    /**
     * Method under test: {@link StoreController#getStoreItemsSorted(String, Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetStoreItemsSorted() throws Exception {
        when(itemService.getItemsSortedByStore((Long) any(), (Integer) any(), (Integer) any(), (String) any(),
                (String) any())).thenReturn(new PageableResponse<>());
        when(JwtUtils.verifyId((String) any(), (Long) any(), org.mockito.Mockito.anyBoolean())).thenReturn(true);

        StoreDto storeDto = new StoreDto();
        storeDto.setManager(new ManagerDto());
        when(storeService.getStore((Long) any())).thenReturn(storeDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/get-store-items-sorted/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(storeController)
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
     * Method under test: {@link StoreController#getStoreItemsSorted(String, Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetStoreItemsSorted2() throws Exception {
        when(itemService.getItemsSortedByStore((Long) any(), (Integer) any(), (Integer) any(), (String) any(),
                (String) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        when(JwtUtils.verifyId((String) any(), (Long) any(), org.mockito.Mockito.anyBoolean())).thenReturn(true);

        StoreDto storeDto = new StoreDto();
        storeDto.setManager(new ManagerDto());
        when(storeService.getStore((Long) any())).thenReturn(storeDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/get-store-items-sorted/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link StoreController#getStoreItemsSorted(String, Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetStoreItemsSorted3() throws Exception {
        when(itemService.getItemsSortedByStore((Long) any(), (Integer) any(), (Integer) any(), (String) any(),
                (String) any())).thenReturn(new PageableResponse<>());
        when(JwtUtils.verifyId((String) any(), (Long) any(), org.mockito.Mockito.anyBoolean())).thenReturn(false);

        StoreDto storeDto = new StoreDto();
        storeDto.setManager(new ManagerDto());
        when(storeService.getStore((Long) any())).thenReturn(storeDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/get-store-items-sorted/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }

    /**
     * Method under test: {@link StoreController#getStores(Integer, Integer)}
     */
    @Test
    void testGetStores() throws Exception {
        when(storeService.getStores((Integer) any(), (Integer) any())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/get-store");
        MockMvcBuilders.standaloneSetup(storeController)
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
     * Method under test: {@link StoreController#getStores(Integer, Integer)}
     */
    @Test
    void testGetStores2() throws Exception {
        when(storeService.getStores((Integer) any(), (Integer) any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/get-store");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }


    /**
     * Method under test: {@link StoreController#postExcelRead(String, boolean, Long, MultipartFile)}
     */
    @Test
    void testPostExcelRead4() throws IOException, InterruptedException, ExecutionException {
        when(storeService.getStore((Long) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        assertThrows(ResponseStatusException.class, () -> storeController.postExcelRead("Jwt", true, 123L,
                new MockMultipartFile("Name", "AAAAAAAA".getBytes("UTF-8"))));
        verify(storeService).getStore((Long) any());
    }


    /**
     * Method under test: {@link StoreController#addInventory(String, Long, MultipartFile)}
     */
//    @Test
//    void testAddInventory() throws Exception {
//        when(storeController.postExcelRead(anyString(),true,anyLong(),(MultipartFile) any())).thenReturn(CompletableFuture.completedFuture(anyString()));
//        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/pharmacy/store/add-inventory/{id}", 123L);
//        MockHttpServletRequestBuilder requestBuilder = postResult.param("excelInventoryData", String.valueOf((Object) null))
//                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
//        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
//                .build()
//                .perform(requestBuilder);
//        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(200));
//    }
    @Test
    void testBuyCart() throws Exception {
        doNothing().when(itemService).updateItem((List<ItemDto>) any());

        CartDto cartDto = new CartDto();
        cartDto.setCartId(123L);
        cartDto.setItems(new HashSet<>());
        cartDto.setPrice(10.0d);
        cartDto.setQuantity(1L);
        String content = (new ObjectMapper()).writeValueAsString(cartDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/pharmacy/store/buy-cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"items\":[],\"cartId\":123,\"quantity\":0,\"price\":0.0}"));
    }

    /**
     * Method under test: {@link StoreController#checkout(CartDto)}
     */
    @Test
    void testCheckout() throws Exception {
        CartDto cartDto = new CartDto();
        cartDto.setCartId(123L);
        cartDto.setItems(new HashSet<>());
        cartDto.setPrice(10.0d);
        cartDto.setQuantity(1L);
        String content = (new ObjectMapper()).writeValueAsString(cartDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/pharmacy/store/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"items\":[],\"cartId\":123,\"quantity\":0,\"price\":0.0}"));
    }

    /**
     * Method under test: {@link StoreController#deleteStore(String, Long)}
     */
    @Test
    void testDeleteStore() throws Exception {
        doNothing().when(storeService).deleteStore((Long) any());
        when(storeService.getStore(anyLong())).thenReturn(mockStoreDto);

        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/delete-store/{id}", 123L)
                .header("Authorization", "Bearer QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));

    }

    /**
     * Method under test: {@link StoreController#getManagerStores(String, Long, Integer, Integer)}
     */
    @Test
    void testGetManagerStores() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/get-manager-stores/{id}", "Uri Variables", "Uri Variables")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link StoreController#getStoreById(String, Long)}
     */
    @Test
    void testGetStoreById() throws Exception {
        when(storeService.getStore((Long) any())).thenReturn(new StoreDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/get-store/{id}", "Uri Variables", "Uri Variables")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link StoreController#getStoreInventory(String, Long)}
     */
    @Test
    void testGetStoreInventory() throws Exception {
        when(storeService.getStore((Long) any())).thenReturn(new StoreDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/get-store-inventory/{id}", "Uri Variables", "Uri Variables")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link StoreController#getStoreItems(String, Long, Integer, Integer)}
     */
    @Test
    void testGetStoreItems() throws Exception {
        when(storeService.getStore((Long) any())).thenReturn(new StoreDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/get-store-items/{id}", "Uri Variables", "Uri Variables")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link StoreController#updateInventory(String, Long, MultipartFile)}
     */
    @Test
    void testUpdateInventory() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/pharmacy/store/update-inventory/{id}",
                123L);
        MockHttpServletRequestBuilder requestBuilder = postResult
                .param("excelInventoryData", String.valueOf((Object) null))
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }


    @Test
    void testDeleteStore1() throws Exception {
        doNothing().when(storeService).deleteStore((Long) any());
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(2L));
        when(storeService.getStore((Long) any())).thenReturn(mockStoreDto);
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/pharmacy/store/delete-store/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteStore2() throws Exception {
        doNothing().when(storeService).deleteStore((Long) any());
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(2L));
        when(storeService.getStore(anyLong())).thenReturn(mockStoreDto);

        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/pharmacy/store/delete-store/{id}",
                2L).header("Authorization", token);
        assertThrows(ResponseStatusException.class, () -> storeController.deleteStore(token, 2L));
    }

    @Test
    void testGetStoreById1() throws Exception {
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(2L));
        when(storeService.getStore((Long) any())).thenReturn(mockStoreDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/get-store/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void getManager2() throws Exception {
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(123L));
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(storeService.getStore((Long) any())).thenReturn(mockStoreDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/store/get-store/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        assertThrows(ResponseStatusException.class, () -> storeController.getStoreById(token, 123L));
    }

    @Test
    void testGetTotalStoreById() throws Exception {
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeService.noOfStores(anyLong())).thenReturn(5L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/total-stores/{id}", 1L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("5"));
    }

    @Test
    void getTotalStoresById2() throws Exception {
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(storeService.noOfStores(anyLong())).thenReturn(5L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/total-stores/{id}", 1L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        assertThrows(ResponseStatusException.class, () -> storeController.totalNoOfStores(token, 1L));
    }

    @Test
    void testGetTotalStores() throws Exception {
        when(storeService.noOfStores()).thenReturn(5L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/total-stores");
        MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("5"));
    }

    @Test
    void testGetTotalRevenue() throws Exception {
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeService.totalRevenue(anyLong())).thenReturn(5D);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/total-revenue/{id}", 1L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("5.0"));
    }

    @Test
    void getTotalRevenue1() throws Exception {
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(storeService.totalRevenue(anyLong())).thenReturn(5D);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/total-revenue/{id}", 1L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        assertThrows(ResponseStatusException.class, () -> storeController.totalRevenue(token, 1L));
    }

    @Test
    void testGetStoreItems1() throws Exception {
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(123L));
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeService.getStore(anyLong())).thenReturn(mockStoreDto);
        when(itemService.getItemsByStore(anyLong(), anyInt(), anyInt())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/get-store-items/{id}", 12L).param("pageNumber", "0", "pageSize", "10")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage\":null}"));
    }

    @Test
    void getStoreItems2() throws Exception {
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(123L));
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(storeService.getStore(anyLong())).thenReturn(mockStoreDto);
        when(itemService.getItemsByStore(anyLong(), anyInt(), anyInt())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/get-store-items/{id}", 12L).param("pageNumber", "0", "pageSize", "10")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        assertThrows(ResponseStatusException.class, () -> storeController.getStoreItems(token, 1L, 0, 10));
    }

    @Test
    void getManagerStores() throws Exception {
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(123L));
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeService.getManagerStore(anyLong(), anyInt(), anyInt())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/get-manager-stores/{id}", 12L).param("pageNumber", "0", "pageSize", "10")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage\":null}"));
    }

    @Test
    void getManagerStores2() throws Exception {
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(123L));
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(storeService.getManagerStore(anyLong(), anyInt(), anyInt())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/get-manager-stores/{id}", 12L).param("pageNumber", "0", "pageSize", "10")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        assertThrows(ResponseStatusException.class, () -> storeController.getManagerStores(token, 1L, 0, 10));
    }

    @Test
    void getStoreInventory() throws Exception {
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(123L));
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeService.getStore(anyLong())).thenReturn(mockStoreDto);
        when(itemService.getStoreItems(anyLong())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/get-store-inventory/{id}", 12L).param("pageNumber", "0", "pageSize", "10")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==").contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getStoreInventory1() throws Exception {
        mockStoreDto = new StoreDto();
        mockStoreDto.setManager(new ManagerDto(123L));
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(storeService.getStore(anyLong())).thenReturn(mockStoreDto);
        when(itemService.getStoreItems(anyLong())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/store/get-store-inventory/{id}", 12L).param("pageNumber", "0", "pageSize", "10")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==").contentType(MediaType.APPLICATION_JSON);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        assertThrows(ResponseStatusException.class, () -> storeController.getStoreInventory(token, 1L));
    }

    @Test
    void createStore() throws Exception {
        StoreDto storeDto = new StoreDto();
        storeDto.setAddress(new AddressDto());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        storeDto.setCreatedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        storeDto.setManager(new ManagerDto(123L));
        storeDto.setRevenue(10.0d);
        storeDto.setStoreId(123L);
        storeDto.setStoreName("Store Name");
        String content = (new ObjectMapper()).writeValueAsString(storeDto);
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeService.createStore((StoreDto) any())).thenReturn(storeDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/pharmacy/store/create-store")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==").contentType(MediaType.APPLICATION_JSON).content(content);
        MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void createStore1() throws Exception {
        StoreDto storeDto = new StoreDto();
        storeDto.setAddress(new AddressDto());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        storeDto.setCreatedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        storeDto.setManager(new ManagerDto(123L));
        storeDto.setRevenue(10.0d);
        storeDto.setStoreId(123L);
        storeDto.setStoreName("Store Name");
        String content = (new ObjectMapper()).writeValueAsString(storeDto);
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(storeService.createStore((StoreDto) any())).thenReturn(storeDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/pharmacy/store/create-store")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==").contentType(MediaType.APPLICATION_JSON).content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        assertThrows(ResponseStatusException.class, () -> storeController.createStore(token, storeDto));
    }

    @Test
    void updateStore() throws Exception {
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        StoreDto storeDto = new StoreDto();
        storeDto.setAddress(new AddressDto(1L, "s", "s", "s", 123456, "c"));
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        storeDto.setCreatedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        storeDto.setManager(new ManagerDto(123L));
        storeDto.setRevenue(10.0d);
        storeDto.setStoreId(123L);
        storeDto.setStoreName("Store Name");
        when(storeService.updateStore((StoreDto) any())).thenReturn(storeDto);
        String content = (new ObjectMapper()).writeValueAsString(storeDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/store/update-store")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==").contentType(MediaType.APPLICATION_JSON).content(content);
        MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void updateStore1() throws Exception {
        StoreDto storeDto = new StoreDto();
        storeDto.setAddress(new AddressDto());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        storeDto.setCreatedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        storeDto.setManager(new ManagerDto(123L));
        storeDto.setRevenue(10.0d);
        storeDto.setStoreId(123L);
        storeDto.setStoreName("Store Name");
        String content = (new ObjectMapper()).writeValueAsString(storeDto);
        when(JwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(storeService.updateStore((StoreDto) any())).thenReturn(storeDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/store/update-store")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==").contentType(MediaType.APPLICATION_JSON).content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(storeController)
                .build()
                .perform(requestBuilder);
        assertThrows(ResponseStatusException.class, () -> storeController.updateStore(token, storeDto));
    }


}

