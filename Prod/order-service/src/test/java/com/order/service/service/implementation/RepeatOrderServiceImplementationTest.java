package com.order.service.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.order.service.client.auth.AuthClient;
import com.order.service.client.notify.EmailClient;
import com.order.service.client.pharmacy.ItemClient;
import com.order.service.client.pharmacy.StoreClient;
import com.order.service.controller.OrdersController;
import com.order.service.dto.AddressDto;
import com.order.service.dto.CartDto;
import com.order.service.dto.ItemDto;
import com.order.service.dto.OrderDetailsDto;
import com.order.service.dto.OrdersDto;
import com.order.service.dto.PageableResponse;
import com.order.service.dto.ProductDto;
import com.order.service.dto.RepeatOrderDto;
import com.order.service.dtoexternal.EmailDto;
import com.order.service.dtoexternal.JwtResponse;
import com.order.service.dtoexternal.LoginRequest;
import com.order.service.exception.ResourceException;
import com.order.service.model.Address;
import com.order.service.model.Orders;
import com.order.service.model.RepeatOrder;
import com.order.service.repository.RepeatOrderRepository;
import com.order.service.service.OrdersService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RepeatOrderServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class RepeatOrderServiceImplementationTest {
    @MockBean
    private StoreClient storeClient;

    @MockBean
    private AuthClient authClient;

    @MockBean
    private ItemClient itemClient;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private OrdersController ordersController;

    @MockBean
    private OrdersService ordersService;

    @MockBean
    private RepeatOrderRepository repeatOrderRepository;

    @Autowired
    private RepeatOrderServiceImplementation repeatOrderServiceImplementation;

    @MockBean
    private EmailClient emailClient;

    Address address;

    RepeatOrder repeatOrder;

    @BeforeEach
    public void setup() throws Exception {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(RepeatOrderServiceImplementation.REPEAT_ORDER);
        address.setState("MD");
        address.setStreet(RepeatOrderServiceImplementation.REPEAT_ORDER);

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName(RepeatOrderServiceImplementation.REPEAT_ORDER);
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
    }

    @Test
    void testCreateOptIn() {
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
        assertThrows(ResourceException.class,
                () -> repeatOrderServiceImplementation.createOptIn(new RepeatOrderDto()));
        verify(modelMapper).map((Object) any(), (Class<RepeatOrder>) any());
    }

    @Test
    void testCreateOptIn2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(RepeatOrderServiceImplementation.REPEAT_ORDER);
        address.setState("MD");
        address.setStreet(RepeatOrderServiceImplementation.REPEAT_ORDER);

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName(RepeatOrderServiceImplementation.REPEAT_ORDER);
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(repeatOrder);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");

        RepeatOrder repeatOrder1 = new RepeatOrder();
        repeatOrder1.setAddress(address1);
        repeatOrder1.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder1.setId(123L);
        repeatOrder1.setIntervalInDays(42);
        repeatOrder1.setName("Name");
        repeatOrder1.setNumberOfDeliveries(10);
        repeatOrder1.setRepeatOrderItems(new HashSet<>());
        repeatOrder1.setUserId(123L);
        when(repeatOrderRepository.save((RepeatOrder) any())).thenReturn(repeatOrder1);
        assertThrows(ResourceException.class,
                () -> repeatOrderServiceImplementation.createOptIn(new RepeatOrderDto()));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(repeatOrderRepository).save((RepeatOrder) any());
    }

    @Test
    void testCreateOptIn3() {

        when(modelMapper.map((Object) any(), (Class<RepeatOrder>) any())).thenReturn(repeatOrder);
        when(repeatOrderRepository.save((RepeatOrder) any())).thenReturn(repeatOrder);
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();

        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(null);
        assertNull(repeatOrderServiceImplementation.createOptIn(new RepeatOrderDto()));

        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());

    }

    @Test
    void testCreateOptIn4() {

        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(repeatOrder);
        when(repeatOrderRepository.save((RepeatOrder) any()))
                .thenThrow(new ResourceException(RepeatOrderServiceImplementation.REPEAT_ORDER,
                        RepeatOrderServiceImplementation.REPEAT_ORDER, "Field Value",
                        ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class,
                () -> repeatOrderServiceImplementation.createOptIn(new RepeatOrderDto()));
        verify(modelMapper).map((Object) any(), (Class<RepeatOrder>) any());
        verify(repeatOrderRepository).save((RepeatOrder) any());
    }

   

    @Test
    void testCreateOptIn6() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(RepeatOrderServiceImplementation.REPEAT_ORDER);
        address.setState("MD");
        address.setStreet(RepeatOrderServiceImplementation.REPEAT_ORDER);

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName(RepeatOrderServiceImplementation.REPEAT_ORDER);
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(repeatOrder);
        when(repeatOrderRepository.save((RepeatOrder) any()))
                .thenThrow(new ResourceException(RepeatOrderServiceImplementation.REPEAT_ORDER,
                        RepeatOrderServiceImplementation.REPEAT_ORDER, "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> repeatOrderServiceImplementation.createOptIn(new RepeatOrderDto()));
        verify(modelMapper).map((Object) any(), (Class<RepeatOrder>) any());
        verify(repeatOrderRepository).save((RepeatOrder) any());
    }

    @Test
    void testUpdateOptIn() {
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        Optional<RepeatOrder> ofResult = Optional.of(repeatOrder);
        when(repeatOrderRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class,
                () -> repeatOrderServiceImplementation.updateOptIn(new RepeatOrderDto()));
        verify(modelMapper).map((Object) any(), (Class<RepeatOrder>) any());
        verify(repeatOrderRepository).findById((Long) any());
    }

    @Test
    void testUpdateOptIn2() {
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value",
                        ResourceException.ErrorType.CREATED));

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        Optional<RepeatOrder> ofResult = Optional.of(repeatOrder);
        when(repeatOrderRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class,
                () -> repeatOrderServiceImplementation.updateOptIn(new RepeatOrderDto()));
        verify(modelMapper).map((Object) any(), (Class<RepeatOrder>) any());
        verify(repeatOrderRepository).findById((Long) any());
    }

    @Test
    void testUpdateOptIn3() {
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");

        when(repeatOrderRepository.save((RepeatOrder) any())).thenReturn(repeatOrder);
        when(repeatOrderRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class,
                () -> repeatOrderServiceImplementation.updateOptIn(new RepeatOrderDto()));
        verify(repeatOrderRepository).findById((Long) any());
    }

    @Test
    void testUpdateOptIn4() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        when(modelMapper.map((Object) any(), (Class<RepeatOrder>) any())).thenReturn(repeatOrder);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");

        RepeatOrderDto repeatOrder1 = new RepeatOrderDto();
        repeatOrder1.setAddress(null);
        repeatOrder1.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder1.setId(123L);
        repeatOrder1.setIntervalInDays(42);
        repeatOrder1.setName("Name");
        repeatOrder1.setNumberOfDeliveries(10);
        repeatOrder1.setRepeatOrderItems(new HashSet<>());
        repeatOrder1.setUserId(123L);
        Optional<RepeatOrder> ofResult = Optional.of(repeatOrder);
        when(repeatOrderRepository.save((RepeatOrder) any())).thenReturn(repeatOrder);
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();

        when(repeatOrderRepository.findById(123L)).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(null);
        assertNull(repeatOrderServiceImplementation.updateOptIn(repeatOrder1));

        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    
    @Test
    void testUpdateOptIn5() {
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        when(repeatOrderRepository.save((RepeatOrder) any())).thenReturn(repeatOrder);
        when(repeatOrderRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> repeatOrderServiceImplementation.updateOptIn(new RepeatOrderDto()));
        verify(repeatOrderRepository).findById((Long) any());
    }

    


    
    @Test
    void testGetOptInToSendNotification() {
        when(repeatOrderRepository.findAllByDeliveryDate((LocalDate) any())).thenReturn(new ArrayList<>());
        repeatOrderServiceImplementation.getOptInToSendNotification(LocalDate.ofEpochDay(1L));
        verify(repeatOrderRepository).findAllByDeliveryDate((LocalDate) any());
    }



    @Test
    void testGetOptInToSendNotification4() {
        when(authClient.authenticateUser((LoginRequest) any()))
                .thenReturn(new ResponseEntity<>(new JwtResponse(), HttpStatus.CONTINUE));
        when(modelMapper.map((Object) any(), (Class<RepeatOrderDto>) any())).thenReturn(new RepeatOrderDto());

        ArrayList<RepeatOrder> repeatOrderList = new ArrayList<>();
        repeatOrderList.add(repeatOrder);
        when(repeatOrderRepository.findAllByDeliveryDate((LocalDate) any())).thenReturn(repeatOrderList);
        // assertTrue(repeatOrderServiceImplementation.getOptInToSendNotification(LocalDate.ofEpochDay(1L)).isEmpty());
        repeatOrderServiceImplementation.getOptInToSendNotification(LocalDate.ofEpochDay(1L));
        verify(authClient).authenticateUser((LoginRequest) any());
        verify(modelMapper).map((Object) any(), (Class<RepeatOrderDto>) any());
        verify(repeatOrderRepository).findAllByDeliveryDate((LocalDate) any());
    }

    @Test
    void testGetAllOptInByUserId() {
        ArrayList<RepeatOrder> repeatOrderList = new ArrayList<>();
        when(repeatOrderRepository.findAllByUserId((Long) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(repeatOrderList));
        PageableResponse<RepeatOrderDto> actualAllOptInByUserId = repeatOrderServiceImplementation
                .getAllOptInByUserId(123L, 10, 3);
        assertEquals(repeatOrderList, actualAllOptInByUserId.getData());
        assertEquals(0L, actualAllOptInByUserId.getTotalRecords().longValue());
        assertEquals(1, actualAllOptInByUserId.getTotalPages().intValue());
        assertEquals(0, actualAllOptInByUserId.getPageSize().intValue());
        assertEquals(0, actualAllOptInByUserId.getPageNumber().intValue());
        assertTrue(actualAllOptInByUserId.getIsLastPage());
        verify(repeatOrderRepository).findAllByUserId((Long) any(), (Pageable) any());
    }

    @Test
    void testGetAllOptInByUserId2() {
        when(modelMapper.map((Object) any(), (Class<RepeatOrderDto>) any())).thenReturn(new RepeatOrderDto());

        ArrayList<RepeatOrder> repeatOrderList = new ArrayList<>();
        repeatOrderList.add(repeatOrder);
        PageImpl<RepeatOrder> pageImpl = new PageImpl<>(repeatOrderList);
        when(repeatOrderRepository.findAllByUserId((Long) any(), (Pageable) any())).thenReturn(pageImpl);
        PageableResponse<RepeatOrderDto> actualAllOptInByUserId = repeatOrderServiceImplementation
                .getAllOptInByUserId(123L, 10, 3);
        assertEquals(1, actualAllOptInByUserId.getData().size());
        assertEquals(1L, actualAllOptInByUserId.getTotalRecords().longValue());
        assertEquals(1, actualAllOptInByUserId.getTotalPages().intValue());
        assertEquals(1, actualAllOptInByUserId.getPageSize().intValue());
        assertEquals(0, actualAllOptInByUserId.getPageNumber().intValue());
        assertTrue(actualAllOptInByUserId.getIsLastPage());
        verify(modelMapper).map((Object) any(), (Class<RepeatOrderDto>) any());
        verify(repeatOrderRepository).findAllByUserId((Long) any(), (Pageable) any());
    }

    @Test
    void testGetAllOptInByUserId3() {
        when(modelMapper.map((Object) any(), (Class<RepeatOrderDto>) any())).thenReturn(new RepeatOrderDto());

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);

        ArrayList<RepeatOrder> repeatOrderList = new ArrayList<>();
        repeatOrderList.add(repeatOrder);
        PageImpl<RepeatOrder> pageImpl = new PageImpl<>(repeatOrderList);
        when(repeatOrderRepository.findAllByUserId((Long) any(), (Pageable) any())).thenReturn(pageImpl);
        PageableResponse<RepeatOrderDto> actualAllOptInByUserId = repeatOrderServiceImplementation
                .getAllOptInByUserId(123L, 10, 3);
        assertEquals(1, actualAllOptInByUserId.getData().size());
        assertEquals(1L, actualAllOptInByUserId.getTotalRecords().longValue());
        assertEquals(1, actualAllOptInByUserId.getTotalPages().intValue());
        assertEquals(1, actualAllOptInByUserId.getPageSize().intValue());
        assertEquals(0, actualAllOptInByUserId.getPageNumber().intValue());
        assertTrue(actualAllOptInByUserId.getIsLastPage());
        verify(modelMapper).map((Object) any(), (Class<RepeatOrderDto>) any());
        verify(repeatOrderRepository).findAllByUserId((Long) any(), (Pageable) any());
    }


    @Test
    void testGetOptInById() {
        RepeatOrderDto repeatOrderDto = new RepeatOrderDto();
        when(modelMapper.map((Object) any(), (Class<RepeatOrderDto>) any())).thenReturn(repeatOrderDto);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        Optional<RepeatOrder> ofResult = Optional.of(repeatOrder);
        when(repeatOrderRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(repeatOrderDto, repeatOrderServiceImplementation.getOptInById(123L));
        verify(modelMapper).map((Object) any(), (Class<RepeatOrderDto>) any());
        verify(repeatOrderRepository).findById((Long) any());
    }

    @Test
    void testGetOptInById2() {

        Optional<RepeatOrder> ofResult = Optional.empty();
        when(repeatOrderRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> repeatOrderServiceImplementation.getOptInById(123L));

    }

    @Test
    void testGetOptInById3() {
        when(modelMapper.map((Object) any(), (Class<RepeatOrderDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        Optional<RepeatOrder> ofResult = Optional.of(repeatOrder);
        when(repeatOrderRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> repeatOrderServiceImplementation.getOptInById(123L));
        verify(modelMapper).map((Object) any(), (Class<RepeatOrderDto>) any());
        verify(repeatOrderRepository).findById((Long) any());
    }



    @Test
    void testDeleteOptIn() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        Optional<RepeatOrder> ofResult = Optional.of(repeatOrder);
        doNothing().when(repeatOrderRepository).deleteById((Long) any());
        when(repeatOrderRepository.findById((Long) any())).thenReturn(ofResult);
        repeatOrderServiceImplementation.deleteOptIn(123L);
        verify(repeatOrderRepository).findById((Long) any());
        verify(repeatOrderRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteOptIn2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);
        Optional<RepeatOrder> ofResult = Optional.of(repeatOrder);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value",
                ResourceException.ErrorType.CREATED))
                .when(repeatOrderRepository)
                .deleteById((Long) any());
        when(repeatOrderRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> repeatOrderServiceImplementation.deleteOptIn(123L));
        verify(repeatOrderRepository).findById((Long) any());
        verify(repeatOrderRepository).deleteById((Long) any());
    }

    @Test
    void testGetOptInToSendNotification7() {
        when(authClient.authenticateUser((LoginRequest) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(modelMapper.map((Object) any(), (Class<RepeatOrderDto>) any()))
                .thenThrow(new ResourceException("admin1", "admin1", "Field Value", ResourceException.ErrorType.CREATED));

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(3L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);

        ArrayList<RepeatOrder> repeatOrderList = new ArrayList<>();
        repeatOrderList.add(repeatOrder);
        when(repeatOrderRepository.findAllByDeliveryDate((LocalDate) any())).thenReturn(repeatOrderList);
        assertThrows(ResourceException.class,
                () -> repeatOrderServiceImplementation.getOptInToSendNotification(LocalDate.ofEpochDay(1L)));
        verify(modelMapper).map((Object) any(), (Class<RepeatOrderDto>) any());
        verify(repeatOrderRepository).findAllByDeliveryDate((LocalDate) any());
    }


    @Test
    void testGetOptInToSendNotification10() {
        doNothing().when(authClient).logout((String) any());
        when(authClient.authenticateUser((LoginRequest) any()))
                .thenReturn(new ResponseEntity<>(new JwtResponse(), HttpStatus.CONTINUE));
        when(modelMapper.map((Object) any(), (Class<RepeatOrderDto>) any())).thenReturn(new RepeatOrderDto());

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(3L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);

        ArrayList<RepeatOrder> repeatOrderList = new ArrayList<>();
        repeatOrderList.add(repeatOrder);
        when(repeatOrderRepository.findAllByDeliveryDate((LocalDate) any())).thenReturn(repeatOrderList);
        repeatOrderServiceImplementation.getOptInToSendNotification(LocalDate.ofEpochDay(1L));
        verify(authClient).authenticateUser((LoginRequest) any());
        verify(authClient).logout((String) any());
        verify(modelMapper).map((Object) any(), (Class<RepeatOrderDto>) any());
        verify(repeatOrderRepository).findAllByDeliveryDate((LocalDate) any());
    }




    @Test
    void testGetItemByProductId6() {
        when(itemClient.getItemsByProductId((String) any(), (Integer) any(), (Integer) any(), (Long) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ProductDto productDto = mock(ProductDto.class);
        when(productDto.getProductIdFk()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(productDto.getQuantity()).thenReturn(1L);
        assertThrows(ResourceException.class,
                () -> repeatOrderServiceImplementation.getItemByProductId("ABC123", productDto));
        verify(productDto).getProductIdFk();
        verify(productDto).getQuantity();
    }

    @Test
    void testGetItemByProductId7() {
        when(itemClient.getItemsByProductId((String) any(), (Integer) any(), (Integer) any(), (Long) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ProductDto productDto = mock(ProductDto.class);
        when(productDto.getProductIdFk()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(productDto.getQuantity()).thenReturn(0L);
        assertTrue(repeatOrderServiceImplementation.getItemByProductId("ABC123", productDto).isEmpty());
        verify(productDto).getQuantity();
    }

    @Test
    void testDeleteOptIn3() {
        doNothing().when(repeatOrderRepository).deleteById((Long) any());
        when(repeatOrderRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> repeatOrderServiceImplementation.deleteOptIn(123L));
        verify(repeatOrderRepository).findById((Long) any());
    }

    @Test
    void testDeleteAllOptIn() {
        doNothing().when(repeatOrderRepository).deleteAllById((Iterable<Long>) any());
        repeatOrderServiceImplementation.deleteAllOptIn(new ArrayList<>());
        verify(repeatOrderRepository).deleteAllById((Iterable<Long>) any());
    }

   
    @Test
    void testDeleteAllOptIn2() {
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(repeatOrderRepository)
                .deleteAllById((Iterable<Long>) any());
        assertThrows(ResourceException.class, () -> repeatOrderServiceImplementation.deleteAllOptIn(new ArrayList<>()));
        verify(repeatOrderRepository).deleteAllById((Iterable<Long>) any());
    }

   
    @Test
    void testDeleteOptInHistory() {
        when(repeatOrderRepository.findAllByUserId((Long) any())).thenReturn(new ArrayList<>());
        doNothing().when(repeatOrderRepository).deleteAll((Iterable<RepeatOrder>) any());
        repeatOrderServiceImplementation.deleteOptInHistory(123L);
        verify(repeatOrderRepository).findAllByUserId((Long) any());
        verify(repeatOrderRepository).deleteAll((Iterable<RepeatOrder>) any());
    }

   
    @Test
    void testDeleteOptInHistory2() {
        when(repeatOrderRepository.findAllByUserId((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(repeatOrderRepository)
                .deleteAll((Iterable<RepeatOrder>) any());
        assertThrows(ResourceException.class, () -> repeatOrderServiceImplementation.deleteOptInHistory(123L));
        verify(repeatOrderRepository).findAllByUserId((Long) any());
    }
    
    @Test
    void testGetOptInToSendNotification15() {
        doNothing().when(authClient).logout((String) any());
        when(authClient.authenticateUser((LoginRequest) any()))
                .thenReturn(new ResponseEntity<>(new JwtResponse(), HttpStatus.CONTINUE));
        AddressDto address = new AddressDto();
        

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");

        ProductDto product =new ProductDto();
        product.setProductIdFk(1L);
        product.setQuantity(2L);
        Set<ProductDto> productSet=new HashSet<>();
        productSet.add(product);
        RepeatOrder repeatOrder = new RepeatOrder();
        repeatOrder.setAddress(address1);
        repeatOrder.setDeliveryDate(LocalDate.ofEpochDay(3L));
        repeatOrder.setId(123L);
        repeatOrder.setIntervalInDays(42);
        repeatOrder.setName("Name");
        repeatOrder.setNumberOfDeliveries(10);
        repeatOrder.setRepeatOrderItems(new HashSet<>());
        repeatOrder.setUserId(123L);

        ArrayList<RepeatOrder> repeatOrderList = new ArrayList<>();
        repeatOrderList.add(repeatOrder);

        Address address2 = new Address();
        address2.setAddressId(123L);
        address2.setCity("Oxford");
        address2.setCountry("GB");
        address2.setPinCode("Pin Code");
        address2.setState("MD");
        address2.setStreet("Street");

        RepeatOrder repeatOrder1 = new RepeatOrder();
        repeatOrder1.setAddress(address2);
        repeatOrder1.setDeliveryDate(LocalDate.ofEpochDay(1L));
        repeatOrder1.setId(123L);
        repeatOrder1.setIntervalInDays(42);
        repeatOrder1.setName("Name");
        repeatOrder1.setNumberOfDeliveries(10);
        repeatOrder1.setRepeatOrderItems(new HashSet<>());
        repeatOrder1.setUserId(123L);
        Optional<RepeatOrder> ofResult = Optional.of(repeatOrder1);
        
        OrdersDto orders = new OrdersDto();
        orders.setItems(new HashSet<>());
        orders.setOrderAddress(address);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        orders.setOrderDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        orders.setOrderDetails(new OrderDetailsDto());
        orders.setOrderId(123L);
        orders.setPrice(10.0d);
        orders.setQuantity(3L);
        orders.setUserId(123L);
        
        when(modelMapper.map((Object) any(), (Class<RepeatOrderDto>) any()))
        .thenReturn(new RepeatOrderDto(123L, 123L, "admin1", 10, 42, LocalDate.ofEpochDay(1L), address, productSet));
        when(ordersService.createOrder((OrdersDto) any())).thenReturn(orders);
        when(repeatOrderRepository.findById((Long) any())).thenReturn(ofResult);
        when(repeatOrderRepository.findAllByDeliveryDate((LocalDate) any())).thenReturn(repeatOrderList);
        ItemDto item = new ItemDto();
      item.setItemId(123L);
      item.setPrice(23D);
      item.setItemQuantity(20L);
      List<ItemDto> itemList = new ArrayList<>();
      itemList.add(item);

      when(itemClient.getItemsByProductId((String) any(),(Integer) any(), (Integer) any(), (Long) any())).thenReturn(
                      new ResponseEntity<>(
                                      new PageableResponse<>(new ArrayList<>(itemList), 10, 3, 1L, 1, true),
                                      HttpStatus.CONTINUE));
        when(storeClient.buyCart((String) any(), (CartDto) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(ordersService.createOrder((OrdersDto) any())).thenReturn(orders);
        when(repeatOrderRepository.save((RepeatOrder) any())).thenReturn(repeatOrder);
        when(emailClient.sendBulkEmail((String) any(), (List<EmailDto>) any()))
      .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(repeatOrderRepository.findById(123L)).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(null);
        when(repeatOrderServiceImplementation.updateOptIn(new RepeatOrderDto())).thenReturn(new RepeatOrderDto(123L, 123L, "admin1", 10, 42, LocalDate.ofEpochDay(1L), address, productSet));
        repeatOrderServiceImplementation.getOptInToSendNotification(LocalDate.ofEpochDay(1L));

        verify(authClient).authenticateUser((LoginRequest) any());

    }
    
    @Test
    void testGetItemByProductId8() {
    	ItemDto item = new ItemDto();
        item.setItemId(123L);
        item.setPrice(23D);
        item.setItemQuantity(20L);
        List<ItemDto> itemList = new ArrayList<>();
        itemList.add(item);

        when(itemClient.getItemsByProductId((String) any(),(Integer) any(), (Integer) any(), (Long) any())).thenReturn(
                        new ResponseEntity<>(
                                        new PageableResponse<>(new ArrayList<>(itemList), 10, 3, 1L, 1, true),
                                        HttpStatus.CONTINUE));
        ProductDto product =new ProductDto();
        product.setProductIdFk(1L);
        product.setQuantity(20L);
      //  when(productDto.getQuantity()).thenReturn(0L);
        assertFalse(repeatOrderServiceImplementation.getItemByProductId("ABC123", product).isEmpty());
       // verify(productDto).getQuantity();
    }
    
    @Test
    void testGetItemByProductId9() {
    	ItemDto item = new ItemDto();
        item.setItemId(123L);
        item.setPrice(23D);
        item.setItemQuantity(20L);
        List<ItemDto> itemList = new ArrayList<>();
        itemList.add(item);

        when(itemClient.getItemsByProductId((String) any(),(Integer) any(), (Integer) any(), (Long) any())).thenReturn(
                        new ResponseEntity<>(
                                        new PageableResponse<>(new ArrayList<>(), 10, 3, 1L, 1, true),
                                        HttpStatus.CONTINUE));
        ProductDto product =new ProductDto();
        product.setProductIdFk(1L);
        product.setQuantity(20L);
      //  when(productDto.getQuantity()).thenReturn(0L);
        assertTrue(repeatOrderServiceImplementation.getItemByProductId("ABC123", product).isEmpty());
       // verify(productDto).getQuantity();
    }
}
