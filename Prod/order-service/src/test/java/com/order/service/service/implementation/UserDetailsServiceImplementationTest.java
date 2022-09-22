package com.order.service.service.implementation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.order.service.dto.AddressDto;
import com.order.service.dto.UserDetailsDto;
import com.order.service.exception.ResourceException;
import com.order.service.model.Address;
import com.order.service.model.Cart;
import com.order.service.model.UserDetails;
import com.order.service.repository.UserDetailsRepository;
import com.order.service.service.CartService;
import com.order.service.service.OrdersService;
import com.order.service.service.RepeatOrderService;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = { UserDetailsServiceImplementation.class })
@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplementationTest {
    @MockBean
    private CartService cartService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private OrdersService ordersService;

    @MockBean
    private RepeatOrderService repeatOrderService;

    @MockBean
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    @Test
    void testGetUserDetails() {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        when(modelMapper.map((Object) any(), (Class<UserDetailsDto>) any())).thenReturn(userDetailsDto);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street1");

        UserDetails userDetails = new UserDetails();
        userDetails.setAddress(address);
        userDetails.setFullName("Dr Jane Doe");
        userDetails.setMobileNumber("42");
        userDetails.setUserId(123L);
        Optional<UserDetails> ofResult = Optional.of(userDetails);
        when(userDetailsRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(userDetailsDto, userDetailsServiceImplementation.getUserDetails(123L));
        verify(modelMapper).map((Object) any(), (Class<UserDetailsDto>) any());
        verify(userDetailsRepository).findById((Long) any());
    }

    @Test
    void testGetUserDetails2() {
        when(modelMapper.map((Object) any(), (Class<UserDetailsDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value",
                        ResourceException.ErrorType.CREATED));

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        UserDetails userDetails = new UserDetails();
        userDetails.setAddress(address);
        userDetails.setFullName("Dr Jane Doe");
        userDetails.setMobileNumber("42");
        userDetails.setUserId(123L);
        Optional<UserDetails> ofResult = Optional.of(userDetails);
        when(userDetailsRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> userDetailsServiceImplementation.getUserDetails(123L));
        verify(modelMapper).map((Object) any(), (Class<UserDetailsDto>) any());
        verify(userDetailsRepository).findById((Long) any());
    }

    @Test
    void testCreateUserDetails3() {
        // UserDetailsDto userDetailsDto = new UserDetailsDto();

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        UserDetailsDto userDetails = new UserDetailsDto();
        userDetails.setAddress(new AddressDto());
        userDetails.setFullName("Dr Jane Doe");
        userDetails.setMobileNumber("42");
        userDetails.setUserId(123L);
        // Optional<UserDetails> ofResult = Optional.of(userDetails);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");

        UserDetails userDetails1 = new UserDetails();
        userDetails1.setAddress(address1);
        userDetails1.setFullName("Dr Jane Doe");
        userDetails1.setMobileNumber("42");
        userDetails1.setUserId(123L);
        when(modelMapper.map(AddressDto.class, Address.class)).thenReturn(null);

        when(cartService.getIfExistsElseCreate((Long) any())).thenReturn(new Cart());

        when(userDetailsRepository.save((UserDetails) any())).thenReturn(userDetails1);

        when(modelMapper.map(userDetails, UserDetails.class)).thenReturn(userDetails1);

        assertNull(userDetailsServiceImplementation.createUserDetails(123L, userDetails));
    }

    @Test
    void testCreateUserDetails4() {
        // UserDetailsDto userDetailsDto = new UserDetailsDto();

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        UserDetailsDto userDetails = new UserDetailsDto();
        userDetails.setAddress(new AddressDto());
        userDetails.setFullName("Dr Jane Doe");
        userDetails.setMobileNumber("42");
        userDetails.setUserId(123L);
        // Optional<UserDetails> ofResult = Optional.of(userDetails);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");

        UserDetails userDetails1 = new UserDetails();
        userDetails1.setAddress(address1);
        userDetails1.setFullName("Dr Jane Doe");
        userDetails1.setMobileNumber("42");
        userDetails1.setUserId(123L);
        when(modelMapper.map(AddressDto.class, Address.class)).thenReturn(null);

        when(cartService.getIfExistsElseCreate((Long) any())).thenReturn(new Cart());

        when(userDetailsRepository.save((UserDetails) any())).thenReturn(userDetails1);

        when(modelMapper.map(userDetails, UserDetails.class)).thenReturn(userDetails1);
        when(userDetailsRepository.save((UserDetails) any())).thenThrow(new ResourceException("Resource Name",
                "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class,
                () -> userDetailsServiceImplementation.createUserDetails(123L, userDetails));

        // assertNull(userDetailsServiceImplementation.createUserDetails(123L,
        // userDetails));
    }

    @Test
    void testUpdateUserDetails() {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        when(modelMapper.map((Object) any(), (Class<UserDetailsDto>) any())).thenReturn(userDetailsDto);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        UserDetails userDetails = new UserDetails();
        userDetails.setAddress(address);
        userDetails.setFullName("Dr Jane Doe");
        userDetails.setMobileNumber("42");
        userDetails.setUserId(123L);
        Optional<UserDetails> ofResult = Optional.of(userDetails);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");

        UserDetails userDetails1 = new UserDetails();
        userDetails1.setAddress(address1);
        userDetails1.setFullName("Dr Jane Doe");
        userDetails1.setMobileNumber("42");
        userDetails1.setUserId(123L);
        when(userDetailsRepository.save((UserDetails) any())).thenReturn(userDetails1);
        when(userDetailsRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(userDetailsDto, userDetailsServiceImplementation.updateUserDetails(new UserDetailsDto()));
        verify(modelMapper).map((Object) any(), (Class<UserDetailsDto>) any());
        verify(userDetailsRepository).save((UserDetails) any());
        verify(userDetailsRepository).findById((Long) any());
    }

    @Test
    void testDeleteUserDetails() {
        doNothing().when(cartService).deleteCart((Long) any());
        doNothing().when(ordersService).deleteOrderHistory((Long) any());
        doNothing().when(repeatOrderService).deleteOptInHistory((Long) any());
        doNothing().when(userDetailsRepository).deleteById((Long) any());
        when(userDetailsRepository.existsById((Long) any())).thenReturn(true);
        userDetailsServiceImplementation.deleteUserDetails(123L);
        verify(cartService).deleteCart((Long) any());
        verify(ordersService).deleteOrderHistory((Long) any());
        verify(repeatOrderService).deleteOptInHistory((Long) any());
        verify(userDetailsRepository).existsById((Long) any());
        verify(userDetailsRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteUserDetails2() {
        doNothing().when(cartService).deleteCart((Long) any());
        doNothing().when(ordersService).deleteOrderHistory((Long) any());
        doNothing().when(repeatOrderService).deleteOptInHistory((Long) any());
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value",
                ResourceException.ErrorType.CREATED))
                .when(userDetailsRepository)
                .deleteById((Long) any());
        when(userDetailsRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(ResourceException.class, () -> userDetailsServiceImplementation.deleteUserDetails(123L));
        verify(cartService).deleteCart((Long) any());
        verify(ordersService).deleteOrderHistory((Long) any());
        verify(repeatOrderService).deleteOptInHistory((Long) any());
        verify(userDetailsRepository).existsById((Long) any());
        verify(userDetailsRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteUserDetails3() {
        doNothing().when(cartService).deleteCart((Long) any());
        doNothing().when(ordersService).deleteOrderHistory((Long) any());
        doNothing().when(repeatOrderService).deleteOptInHistory((Long) any());
        doNothing().when(userDetailsRepository).deleteById((Long) any());
        when(userDetailsRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(ResourceException.class, () -> userDetailsServiceImplementation.deleteUserDetails(123L));
        verify(userDetailsRepository).existsById((Long) any());
    }

    @Test
    void testCheckIfExists() {
        when(userDetailsRepository.existsById((Long) any())).thenReturn(true);
        assertTrue(userDetailsServiceImplementation.checkIfExists(123L));
        verify(userDetailsRepository).existsById((Long) any());
    }

    @Test
    void testCheckIfExists2() {
        when(userDetailsRepository.existsById((Long) any())).thenReturn(false);
        assertFalse(userDetailsServiceImplementation.checkIfExists(123L));
        verify(userDetailsRepository).existsById((Long) any());
    }

    @Test
    void testCheckIfExists3() {
        when(userDetailsRepository.existsById((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value",
                        ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> userDetailsServiceImplementation.checkIfExists(123L));
        verify(userDetailsRepository).existsById((Long) any());
    }
}
