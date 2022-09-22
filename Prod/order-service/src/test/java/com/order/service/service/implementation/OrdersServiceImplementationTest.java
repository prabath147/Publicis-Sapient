package com.order.service.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.order.service.dto.OrdersDto;
import com.order.service.dto.PageableResponse;
import com.order.service.exception.ResourceException;
import com.order.service.model.Address;
import com.order.service.model.OrderDetails;
import com.order.service.model.Orders;
import com.order.service.repository.OrdersRepository;
import com.order.service.utils.JwtUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ContextConfiguration(classes = {OrdersServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class OrdersServiceImplementationTest {
    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private OrdersRepository ordersRepository;
    
    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private OrdersServiceImplementation ordersServiceImplementation;

    @Test
    void testCreateOrder() {
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
        assertThrows(ResourceException.class, () -> ordersServiceImplementation.createOrder(new OrdersDto()));
        verify(modelMapper).map((Object) any(), (Class<Orders>) any());
    }

    @Test
    void testCreateOrder2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Order");
        address.setState("MD");
        address.setStreet("Order");

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsId(123L);

        Orders orders = new Orders();
        orders.setItems(new HashSet<>());
        orders.setOrderAddress(address);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders.setOrderDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        orders.setOrderDetails(orderDetails);
        orders.setOrderId(123L);
        orders.setPrice(10.0d);
        orders.setQuantity(3L);
        orders.setUserId(123L);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(orders);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");

        OrderDetails orderDetails1 = new OrderDetails();
        orderDetails1.setOrderDetailsId(123L);

        Orders orders1 = new Orders();
        orders1.setItems(new HashSet<>());
        orders1.setOrderAddress(address1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders1.setOrderDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        orders1.setOrderDetails(orderDetails1);
        orders1.setOrderId(123L);
        orders1.setPrice(10.0d);
        orders1.setQuantity(1L);
        orders1.setUserId(123L);
        when(ordersRepository.save((Orders) any())).thenReturn(orders1);
        assertThrows(ResourceException.class, () -> ordersServiceImplementation.createOrder(new OrdersDto()));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(ordersRepository).save((Orders) any());
    }

    @Test
    void testCreateOrder3() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Order");
        address.setState("MD");
        address.setStreet("Order");

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsId(123L);

        Orders orders = new Orders();
        orders.setItems(new HashSet<>());
        orders.setOrderAddress(address);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders.setOrderDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        orders.setOrderDetails(orderDetails);
        orders.setOrderId(123L);
        orders.setPrice(10.0d);
        orders.setQuantity(3L);
        orders.setUserId(123L);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(orders);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");

        OrderDetails orderDetails1 = new OrderDetails();
        orderDetails1.setOrderDetailsId(123L);

        Orders orders1 = new Orders();
        orders1.setItems(new HashSet<>());
        orders1.setOrderAddress(address1);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders1.setOrderDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        orders1.setOrderDetails(orderDetails1);
        orders1.setOrderId(123L);
        orders1.setPrice(10.0d);
        orders1.setQuantity(1L);
        orders1.setUserId(123L);
        when(ordersRepository.save((Orders) any())).thenReturn(orders1);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(null);
        assertNull(ordersServiceImplementation.createOrder(new OrdersDto()));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(ordersRepository).save((Orders) any());
    }


    @Test
    void testGetOrder() {
        OrdersDto ordersDto = new OrdersDto();
        when(modelMapper.map((Object) any(), (Class<OrdersDto>) any())).thenReturn(ordersDto);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsId(123L);

        Orders orders = new Orders();
        orders.setItems(new HashSet<>());
        orders.setOrderAddress(address);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders.setOrderDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        orders.setOrderDetails(orderDetails);
        orders.setOrderId(123L);
        orders.setPrice(10.0d);
        orders.setQuantity(1L);
        orders.setUserId(123L);
        Optional<Orders> ofResult = Optional.of(orders);
        when(ordersRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(ordersDto, ordersServiceImplementation.getOrder(123L));
        verify(modelMapper).map((Object) any(), (Class<OrdersDto>) any());
        verify(ordersRepository).findById((Long) any());
    }

    @Test
    void testGetOrder2() {
        when(modelMapper.map((Object) any(), (Class<OrdersDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsId(123L);

        Orders orders = new Orders();
        orders.setItems(new HashSet<>());
        orders.setOrderAddress(address);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders.setOrderDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        orders.setOrderDetails(orderDetails);
        orders.setOrderId(123L);
        orders.setPrice(10.0d);
        orders.setQuantity(1L);
        orders.setUserId(123L);
        Optional<Orders> ofResult = Optional.of(orders);
        when(ordersRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> ordersServiceImplementation.getOrder(123L));
        verify(modelMapper).map((Object) any(), (Class<OrdersDto>) any());
        verify(ordersRepository).findById((Long) any());
    }

    @Test
    void testGetOrder3() {

        when(ordersRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> ordersServiceImplementation.getOrder(123L));

    }

    

    @Test
    void testGetOrders() {
        ArrayList<Orders> ordersList = new ArrayList<>();
        when(ordersRepository.findAllByUserId((Long) any(), (Pageable) any())).thenReturn(new PageImpl<>(ordersList));
        PageableResponse<OrdersDto> actualOrders = ordersServiceImplementation.getOrders(123L, 10, 3);
        assertEquals(ordersList, actualOrders.getData());
        assertEquals(0L, actualOrders.getTotalRecords().longValue());
        assertEquals(1, actualOrders.getTotalPages().intValue());
        assertEquals(0, actualOrders.getPageSize().intValue());
        assertEquals(0, actualOrders.getPageNumber().intValue());
        assertTrue(actualOrders.getIsLastPage());
        verify(ordersRepository).findAllByUserId((Long) any(), (Pageable) any());
    }

    @Test
    void testGetOrders2() {
        when(modelMapper.map((Object) any(), (Class<OrdersDto>) any())).thenReturn(new OrdersDto());

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsId(123L);

        Orders orders = new Orders();
        orders.setItems(new HashSet<>());
        orders.setOrderAddress(address);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders.setOrderDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        orders.setOrderDetails(orderDetails);
        orders.setOrderId(123L);
        orders.setPrice(10.0d);
        orders.setQuantity(1L);
        orders.setUserId(123L);

        ArrayList<Orders> ordersList = new ArrayList<>();
        ordersList.add(orders);
        PageImpl<Orders> pageImpl = new PageImpl<>(ordersList);
        when(ordersRepository.findAllByUserId((Long) any(), (Pageable) any())).thenReturn(pageImpl);
        PageableResponse<OrdersDto> actualOrders = ordersServiceImplementation.getOrders(123L, 10, 3);
        assertEquals(1, actualOrders.getData().size());
        assertEquals(1L, actualOrders.getTotalRecords().longValue());
        assertEquals(1, actualOrders.getTotalPages().intValue());
        assertEquals(1, actualOrders.getPageSize().intValue());
        assertEquals(0, actualOrders.getPageNumber().intValue());
        assertTrue(actualOrders.getIsLastPage());
        verify(modelMapper).map((Object) any(), (Class<OrdersDto>) any());
        verify(ordersRepository).findAllByUserId((Long) any(), (Pageable) any());
    }

    


    @Test
    void testDeleteOrder() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsId(123L);

        Orders orders = new Orders();
        orders.setItems(new HashSet<>());
        orders.setOrderAddress(address);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders.setOrderDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        orders.setOrderDetails(orderDetails);
        orders.setOrderId(123L);
        orders.setPrice(10.0d);
        orders.setQuantity(1L);
        orders.setUserId(123L);
        Optional<Orders> ofResult = Optional.of(orders);
        doNothing().when(ordersRepository).deleteById((Long) any());
        when(ordersRepository.findById((Long) any())).thenReturn(ofResult);
        ordersServiceImplementation.deleteOrder(123L);
        verify(ordersRepository).findById((Long) any());
        verify(ordersRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteOrder2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsId(123L);

        Orders orders = new Orders();
        orders.setItems(new HashSet<>());
        orders.setOrderAddress(address);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders.setOrderDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        orders.setOrderDetails(orderDetails);
        orders.setOrderId(123L);
        orders.setPrice(10.0d);
        orders.setQuantity(1L);
        orders.setUserId(123L);
        Optional<Orders> ofResult = Optional.of(orders);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(ordersRepository)
                .deleteById((Long) any());
        when(ordersRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> ordersServiceImplementation.deleteOrder(123L));
        verify(ordersRepository).findById((Long) any());
        verify(ordersRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteOrder3() {
        doNothing().when(ordersRepository).deleteById((Long) any());
        when(ordersRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> ordersServiceImplementation.deleteOrder(123L));
        verify(ordersRepository).findById((Long) any());
    }

    
    @Test
    void testDeleteOrder4() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsId(123L);

        Orders orders = new Orders();
        orders.setItems(new HashSet<>());
        orders.setOrderAddress(address);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders.setOrderDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        orders.setOrderDetails(orderDetails);
        orders.setOrderId(123L);
        orders.setPrice(10.0d);
        orders.setQuantity(1L);
        orders.setUserId(123L);
        Optional<Orders> ofResult = Optional.of(orders);
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(ordersRepository)
                .deleteById((Long) org.mockito.Mockito.any());
        when(ordersRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> ordersServiceImplementation.deleteOrder(123L));
        verify(ordersRepository).findById((Long) org.mockito.Mockito.any());
        verify(ordersRepository).deleteById((Long) org.mockito.Mockito.any());
    }

    @Test
    void testDeleteAllOrder2() {
        when(ordersRepository.findAllByOrderIdIn((List<Long>) org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        doNothing().when(ordersRepository).deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
        ordersServiceImplementation.deleteAllOrder("Jwt", new ArrayList<>());
        verify(ordersRepository).findAllByOrderIdIn((List<Long>) org.mockito.Mockito.any());
        verify(ordersRepository).deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
    }

    
    @Test
    void testDeleteAllOrder4() {
        when(ordersRepository.findAllByOrderIdIn((List<Long>) org.mockito.Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(ordersRepository)
                .deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
        assertThrows(ResponseStatusException.class,
                () -> ordersServiceImplementation.deleteAllOrder("Jwt", new ArrayList<>()));
        verify(ordersRepository).findAllByOrderIdIn((List<Long>) org.mockito.Mockito.any());
    }

    
    @Test
    void testDeleteAllOrder6() {
        doNothing().when(ordersRepository).deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
        ordersServiceImplementation.deleteAllOrder(new ArrayList<>());
        verify(ordersRepository).deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
    }

   
    @Test
    void testDeleteAllOrder7() {
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(ordersRepository)
                .deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
        assertThrows(ResourceException.class, () -> ordersServiceImplementation.deleteAllOrder(new ArrayList<>()));
        verify(ordersRepository).deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
    }

   
    @Test
    void testDeleteAllOrder8() {
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(ordersRepository)
                .deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
        assertThrows(ResourceException.class, () -> ordersServiceImplementation.deleteAllOrder(new ArrayList<>()));
        verify(ordersRepository).deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
    }

    
    @Test
    void testDeleteOrderHistory() {
        when(ordersRepository.findAllByUserIdEquals((Long) org.mockito.Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(ordersRepository)
                .deleteAll((Iterable<Orders>) org.mockito.Mockito.any());
        assertThrows(ResponseStatusException.class, () -> ordersServiceImplementation.deleteOrderHistory(123L));
        verify(ordersRepository).findAllByUserIdEquals((Long) org.mockito.Mockito.any());
    }

    @Test
    void testDeleteAllOrder() {
        when(ordersRepository.findAllByUserIdEquals((Long) any())).thenReturn(new ArrayList<>());
        doNothing().when(ordersRepository).deleteAll((Iterable<Orders>) any());
        ordersServiceImplementation.deleteOrderHistory(123L);
        verify(ordersRepository).findAllByUserIdEquals((Long) any());
        verify(ordersRepository).deleteAll((Iterable<Orders>) any());
    }



}

