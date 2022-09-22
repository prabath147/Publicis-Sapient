package com.order.service.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.service.client.pharmacy.StoreClient;
import com.order.service.dto.CartDto;
import com.order.service.dto.ItemDto;
import com.order.service.dto.OrdersDto;
import com.order.service.exception.ResourceException;
import com.order.service.service.CartService;
import com.order.service.utils.JwtUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {CartController.class})
@ExtendWith(SpringExtension.class)
class CartControllerTest {
    @Autowired
    private CartController cartController;

    @MockBean
    private CartService cartService;
    
    @MockBean
    private StoreClient storeClient;
    
    @MockBean
    JwtUtils jwtUtils;
    
    private static final String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjEiLCJpYXQiOjE2NjI5NTY5MzQsImV4cCI6MTY2Mjk2MDUzNCwicm9sZSI6IlJPTEVfQURNSU4iLCJpZCI6Mn0.OmKp6M8jw3um6E-OpSrMTu-JmYg0pj00lMJMxGBoM2JYsqmqIVOOVX-fpm-yQ_d7nRw33s1j8V9ggAMDH9xSaw";


    @Test
    void testGetCart() throws Exception {
        when(cartService.getCart((Long) any())).thenReturn(new CartDto());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/order/cart/get-cart/{id}", 2L)
        		.header("Authorization", token);
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"cartId\":null,\"items\":[],\"quantity\":null,\"price\":null}"));
    }
    
    @Test
    void testGetCart1() throws Exception {
        when(cartService.getCart((Long) any())).thenReturn(new CartDto());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/order/cart/get-cart/{id}", 2L)
        		.header("Authorization", token);
        assertThrows(ResponseStatusException.class, () -> cartController.getCart(token,2L));

    }

    @Test
    void testAddPrescriptionToCart() throws Exception {
        when(cartService.addToCart((Long) any(), (List<ItemDto>) any())).thenReturn(new CartDto());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);

        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/order/cart/add-prescription-to-cart/{id}", 2L)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new ArrayList<>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"cartId\":null,\"items\":[],\"quantity\":null,\"price\":null}"));
    }
    
    @Test
    void testAddPrescriptionToCart1() throws Exception {
        when(cartService.addToCart((Long) any(), (List<ItemDto>) any())).thenReturn(new CartDto());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);

        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/order/cart/add-prescription-to-cart/{id}", 2L)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON);

        assertThrows(ResponseStatusException.class, () -> cartController.addPrescriptionToCart(token, 2L, null));

    }

    @Test
    void testAddToCart() throws Exception {
        when(cartService.addToCart((Long) any(), (ItemDto) any())).thenReturn(new CartDto());

        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(123L);
        itemDto.setItemIdFk(42L);
        itemDto.setItemQuantity(42L);
        itemDto.setPrice(10.0d);
       // itemDto.setItemQuantity(1);
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);

        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/order/cart/add-to-cart/{id}", 2L)
        		.header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"cartId\":null,\"items\":[],\"quantity\":null,\"price\":null}"));
    }
    
    @Test
    void testAddToCart1() throws Exception {
        when(cartService.addToCart((Long) any(), (ItemDto) any())).thenReturn(new CartDto());

        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(123L);
        itemDto.setItemIdFk(42L);
        itemDto.setItemQuantity(42L);
        itemDto.setPrice(10.0d);
       // itemDto.setItemQuantity(1);
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);

        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/order/cart/add-to-cart/{id}", 2L)
        		.header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        assertThrows(ResponseStatusException.class, () -> cartController.addToCart(content, 2L, itemDto));

    }

    @Test
    void testEmptyCart() throws Exception {
        when(cartService.emptyCart((Long) any())).thenReturn(new CartDto());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/order/cart/empty-cart/{id}", 2L)
        		.header("Authorization", token);
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"cartId\":null,\"items\":[],\"quantity\":null,\"price\":null}"));
    }
    
    @Test
    void testEmptyCart1() throws Exception {
        when(cartService.emptyCart((Long) any())).thenReturn(new CartDto());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/order/cart/empty-cart/{id}", 2L)
        		.header("Authorization", token);
        assertThrows(ResponseStatusException.class, () -> cartController.emptyCart(token, 2L));

    }

    @Test
    void testDeleteItemFromCart() throws Exception {
        when(cartService.removeFromCart((Long) any(), (ItemDto) any())).thenReturn(new CartDto());

        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(123L);
        itemDto.setItemIdFk(42L);
        itemDto.setItemQuantity(42L);
        itemDto.setPrice(10.0d);
       // itemDto.setQuantity(1);
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/order/cart/remove-from-cart/{id}", 2L)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"cartId\":null,\"items\":[],\"quantity\":null,\"price\":null}"));
    }
    
    @Test
    void testDeleteItemFromCart1() throws Exception {
        when(cartService.removeFromCart((Long) any(), (ItemDto) any())).thenReturn(new CartDto());

        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(123L);
        itemDto.setItemIdFk(42L);
        itemDto.setItemQuantity(42L);
        itemDto.setPrice(10.0d);
       // itemDto.setQuantity(1);
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/order/cart/remove-from-cart/{id}", 2L)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        assertThrows(ResponseStatusException.class, () -> cartController.deleteItemFromCart(content, 2L, itemDto));

    }

    @Test
    void testPlaceOrder() throws Exception {
        when(cartService.checkout((Long) any(), (CartDto) any())).thenReturn(new CartDto());

        CartDto cartDto = new CartDto();
        cartDto.setCartId(123L);
        cartDto.setItems(new HashSet<>());
        cartDto.setPrice(10.0d);
        cartDto.setQuantity(1L);
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeClient.checkout((CartDto) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        String content = (new ObjectMapper()).writeValueAsString(cartDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/order/cart/checkout-cart/{id}", 2L)
        		.header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"cartId\":null,\"items\":[],\"quantity\":null,\"price\":null}"));
    }
    
    @Test
    void testPlaceOrder10() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(storeClient.checkout((CartDto) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/order/cart/checkout-items/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashSet<>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }

     
    @Test
    void testPlaceOrder11() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeClient.checkout((CartDto) any())).thenReturn(null);
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/order/cart/checkout-items/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashSet<>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }

   
    @Test
    void testPlaceOrder12() throws Exception {
        doNothing().when(cartService).checkout((Long) any(), (Set<ItemDto>) any());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeClient.checkout((CartDto) any())).thenReturn(new ResponseEntity<>(new CartDto(), HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/order/cart/checkout-items/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashSet<>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"cartId\":null,\"items\":[],\"quantity\":null,\"price\":null}"));
    }

   
    @Test
    void testPlaceOrder13() throws Exception {
        doNothing().when(cartService).checkout((Long) any(), (Set<ItemDto>) any());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeClient.checkout((CartDto) any())).thenReturn(new ResponseEntity<>(new CartDto(), HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/order/cart/checkout-items/{id}", 123L);
        MockHttpServletRequestBuilder contentTypeResult = postResult.header("Authorization", new CartDto())
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashSet<>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"cartId\":null,\"items\":[],\"quantity\":null,\"price\":null}"));
    }

    
    @Test
    void testPlaceOrder14() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(cartService)
                .checkout((Long) any(), (Set<ItemDto>) any());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(storeClient.checkout((CartDto) any())).thenReturn(new ResponseEntity<>(new CartDto(), HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/order/cart/checkout-items/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashSet<>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }
    
    @Test
    void testSubtractFromCart() throws Exception {
        when(cartService.subtractFromCart((Long) any(), (ItemDto) any())).thenReturn(new CartDto());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);

        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(123L);
        itemDto.setItemIdFk(42L);
        itemDto.setItemQuantity(42L);
        itemDto.setPrice(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/cart/subtract-from-cart/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"cartId\":null,\"items\":[],\"quantity\":null,\"price\":null}"));
    }

    
    @Test
    void testSubtractFromCart2() throws Exception {
        when(cartService.subtractFromCart((Long) any(), (ItemDto) any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);

        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(123L);
        itemDto.setItemIdFk(42L);
        itemDto.setItemQuantity(42L);
        itemDto.setPrice(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/cart/subtract-from-cart/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    
    @Test
    void testSubtractFromCart3() throws Exception {
        when(cartService.subtractFromCart((Long) any(), (ItemDto) any())).thenReturn(new CartDto());
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);

        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(123L);
        itemDto.setItemIdFk(42L);
        itemDto.setItemQuantity(42L);
        itemDto.setPrice(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/cart/subtract-from-cart/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }
    
}
    
    
