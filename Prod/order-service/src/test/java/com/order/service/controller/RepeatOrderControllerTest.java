package com.order.service.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.service.dto.AddressDto;
import com.order.service.dto.PageableResponse;
import com.order.service.dto.ProductDto;
import com.order.service.dto.RepeatOrderDto;
import com.order.service.model.Address;
import com.order.service.service.RepeatOrderService;
import com.order.service.utils.JwtUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
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
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {RepeatOrderController.class})
@ExtendWith(SpringExtension.class)
class RepeatOrderControllerTest {
    @Autowired
    private RepeatOrderController repeatOrderController;

    @MockBean
    private RepeatOrderService repeatOrderService;
    
    @MockBean
    JwtUtils jwtUtils;
    
    private static final String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjEiLCJpYXQiOjE2NjI5NTY5MzQsImV4cCI6MTY2Mjk2MDUzNCwicm9sZSI6IlJPTEVfQURNSU4iLCJpZCI6Mn0.OmKp6M8jw3um6E-OpSrMTu-JmYg0pj00lMJMxGBoM2JYsqmqIVOOVX-fpm-yQ_d7nRw33s1j8V9ggAMDH9xSaw";
    @Test
    void testUpdateOptIn() throws Exception {
        when(repeatOrderService.updateOptIn((RepeatOrderDto) any())).thenReturn(new RepeatOrderDto());
        AddressDto address1 = new AddressDto();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("123456");
        address1.setState("MD");
        address1.setStreet("Street");
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();
        repeatOrderDto.setAddress(address1);
        repeatOrderDto.setDeliveryDate(null);
        ProductDto product =new ProductDto();
        product.setProductIdFk(1L);
        product.setQuantity(2L);
        product.setProductId(1L);
        product.setPrice(12D);
        Set<ProductDto> productSet=new HashSet<>();
        productSet.add(product);
        repeatOrderDto.setId(123L);
        repeatOrderDto.setIntervalInDays(42);
        repeatOrderDto.setName("Name");
        repeatOrderDto.setNumberOfDeliveries(10);
        repeatOrderDto.setRepeatOrderItems(productSet);
        repeatOrderDto.setUserId(2L);
        String content = (new ObjectMapper()).writeValueAsString(repeatOrderDto);
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/order/optin/update-optin")
        		.header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(repeatOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"userId\":null,\"name\":null,\"numberOfDeliveries\":0,\"intervalInDays\":0,\"deliveryDate\":null,"
                                        + "\"address\":null,\"repeatOrderItems\":[]}"));
    }
    
    @Test
    void testUpdateOptIn1() throws Exception {
        when(repeatOrderService.updateOptIn((RepeatOrderDto) any())).thenReturn(new RepeatOrderDto());

        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();
        repeatOrderDto.setAddress(new AddressDto());
        repeatOrderDto.setDeliveryDate(null);
        repeatOrderDto.setId(123L);
        repeatOrderDto.setIntervalInDays(42);
        repeatOrderDto.setName("Name");
        repeatOrderDto.setNumberOfDeliveries(10);
        repeatOrderDto.setRepeatOrderItems(new HashSet<>());
        repeatOrderDto.setUserId(2L);
        String content = (new ObjectMapper()).writeValueAsString(repeatOrderDto);
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/order/optin/update-optin")
        		.header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        assertThrows(ResponseStatusException.class, () -> repeatOrderController.updateOptIn(token, repeatOrderDto));

    }
    
    @Test
    void testCreateOptIn() throws Exception {
        when(repeatOrderService.createOptIn((RepeatOrderDto) any())).thenReturn(new RepeatOrderDto());
        AddressDto address1 = new AddressDto();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("123456");
        address1.setState("MD");
        address1.setStreet("Street");
        ProductDto product =new ProductDto();
        product.setProductIdFk(1L);
        product.setQuantity(2L);
        product.setProductId(1L);
        product.setPrice(12D);
        Set<ProductDto> productSet=new HashSet<>();
        productSet.add(product);
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();
        repeatOrderDto.setAddress(address1);
        repeatOrderDto.setDeliveryDate(null);
        repeatOrderDto.setId(123L);
        repeatOrderDto.setIntervalInDays(42);
        repeatOrderDto.setName("Name");
        repeatOrderDto.setNumberOfDeliveries(10);
        repeatOrderDto.setRepeatOrderItems(productSet);
        repeatOrderDto.setUserId(2L);
        String content = (new ObjectMapper()).writeValueAsString(repeatOrderDto);
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/order/optin/create-optin")
        		.header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(repeatOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"userId\":null,\"name\":null,\"numberOfDeliveries\":0,\"intervalInDays\":0,\"deliveryDate\":null,"
                                        + "\"address\":null,\"repeatOrderItems\":[]}"));
    }
    
    @Test
    void testCreateOptIn1() throws Exception {
        when(repeatOrderService.createOptIn((RepeatOrderDto) any())).thenReturn(new RepeatOrderDto());
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();
        repeatOrderDto.setAddress(new AddressDto());
        repeatOrderDto.setDeliveryDate(null);
        repeatOrderDto.setId(123L);
        repeatOrderDto.setIntervalInDays(42);
        repeatOrderDto.setName("Name");
        repeatOrderDto.setNumberOfDeliveries(10);
        repeatOrderDto.setRepeatOrderItems(new HashSet<>());
        repeatOrderDto.setUserId(2L);
        String content = (new ObjectMapper()).writeValueAsString(repeatOrderDto);
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/order/optin/create-optin")
        		.header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        assertThrows(ResponseStatusException.class, () -> repeatOrderController.createOptIn(token, repeatOrderDto));

    }

    @Test
    void testGetOptInById() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();
        repeatOrderDto.setAddress(new AddressDto());
        repeatOrderDto.setDeliveryDate(null);
        repeatOrderDto.setId(123L);
        repeatOrderDto.setIntervalInDays(42);
        repeatOrderDto.setName("Name");
        repeatOrderDto.setNumberOfDeliveries(10);
        repeatOrderDto.setRepeatOrderItems(new HashSet<>());
        repeatOrderDto.setUserId(2L);
        when(repeatOrderService.getOptInById(123L)).thenReturn(repeatOrderDto);

        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/order/optin/{id}", 123L)
        		.header("Authorization", token);
        MockHttpServletRequestBuilder requestBuilder = getResult.param("optinId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(repeatOrderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(200));
    }
    
    @Test
    void testGetOptInById1() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();
        repeatOrderDto.setAddress(new AddressDto());
        repeatOrderDto.setDeliveryDate(null);
        repeatOrderDto.setId(123L);
        repeatOrderDto.setIntervalInDays(42);
        repeatOrderDto.setName("Name");
        repeatOrderDto.setNumberOfDeliveries(10);
        repeatOrderDto.setRepeatOrderItems(new HashSet<>());
        repeatOrderDto.setUserId(2L);
        when(repeatOrderService.getOptInById(123L)).thenReturn(repeatOrderDto);

        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/order/optin/{id}", 123L)
        		.header("Authorization", token);
        MockHttpServletRequestBuilder requestBuilder = getResult.param("optinId", String.valueOf(1L));
        assertThrows(ResponseStatusException.class, () -> repeatOrderController.getOptInById(token, 123L));

    }
    

    @Test
    void testDeleteOptInById() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();
        repeatOrderDto.setAddress(new AddressDto());
        repeatOrderDto.setDeliveryDate(null);
        repeatOrderDto.setId(123L);
        repeatOrderDto.setIntervalInDays(42);
        repeatOrderDto.setName("Name");
        repeatOrderDto.setNumberOfDeliveries(10);
        repeatOrderDto.setRepeatOrderItems(new HashSet<>());
        repeatOrderDto.setUserId(2L);
        when(repeatOrderService.getOptInById(123L)).thenReturn(repeatOrderDto);

        doNothing().when(repeatOrderService).deleteOptIn((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/order/optin/delete/{id}", 123L)
        		.header("Authorization", token);
        MockMvcBuilders.standaloneSetup(repeatOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Optin Order Deleted Successfully!"));
    }
    
    @Test
    void testDeleteOptInById1() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();
        repeatOrderDto.setAddress(new AddressDto());
        repeatOrderDto.setDeliveryDate(null);
        repeatOrderDto.setId(123L);
        repeatOrderDto.setIntervalInDays(42);
        repeatOrderDto.setName("Name");
        repeatOrderDto.setNumberOfDeliveries(10);
        repeatOrderDto.setRepeatOrderItems(new HashSet<>());
        repeatOrderDto.setUserId(2L);
        when(repeatOrderService.getOptInById(123L)).thenReturn(repeatOrderDto);

        doNothing().when(repeatOrderService).deleteOptIn((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/order/optin/delete/{id}", 123L)
        		.header("Authorization", token);
        assertThrows(ResponseStatusException.class, () -> repeatOrderController.deleteOptInById(token, 123L));

    }
    
    @Test
    void testGetOptInByUserId() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);

        when(repeatOrderService.getAllOptInByUserId((Long) any(), (Integer) any(), (Integer) any()))
                .thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/order/optin/user/{id}", 2L)
        		.header("Authorization", token);
        MockMvcBuilders.standaloneSetup(repeatOrderController)
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
    void testGetOptInByUserId1() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);

        when(repeatOrderService.getAllOptInByUserId((Long) any(), (Integer) any(), (Integer) any()))
                .thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/order/optin/user/{id}", 2L)
        		.header("Authorization", token);
        assertThrows(ResponseStatusException.class, () -> repeatOrderController.getOptInByUserId(token, 2L,0,0));

    }
}

