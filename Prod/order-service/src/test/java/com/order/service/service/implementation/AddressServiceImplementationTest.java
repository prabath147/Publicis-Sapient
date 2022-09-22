package com.order.service.service.implementation;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.order.service.dto.AddressDto;
import com.order.service.exception.ResourceException;
import com.order.service.model.Address;
import com.order.service.repository.AddressRepository;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AddressServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class AddressServiceImplementationTest {
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    private AddressServiceImplementation addressServiceImplementation;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void testGetAddress() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        Optional<Address> ofResult = Optional.of(address);
        when(addressRepository.findById((Long) any())).thenReturn(ofResult);
        AddressDto addressDto = new AddressDto();
        when(modelMapper.map((Object) any(), (Class<AddressDto>) any())).thenReturn(addressDto);
        assertSame(addressDto, addressServiceImplementation.getAddress(123L));
        verify(addressRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<AddressDto>) any());
    }

    @Test
    void testGetAddress2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        Optional<Address> ofResult = Optional.of(address);
        when(addressRepository.findById((Long) any())).thenReturn(Optional.empty());
        when(modelMapper.map((Object) any(), (Class<AddressDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.FOUND));
        assertThrows(ResourceException.class, () -> addressServiceImplementation.getAddress(123L));
    }

    @Test
    void testCreateAddress() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        when(addressRepository.save((Address) any())).thenReturn(address);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
        assertThrows(ResourceException.class, () -> addressServiceImplementation.createAddress(new AddressDto()));
        verify(modelMapper).map((Object) any(), (Class<Address>) any());
    }

    @Test
    void testCreateAddress2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        when(addressRepository.save((Address) any())).thenReturn(address);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> addressServiceImplementation.createAddress(new AddressDto()));
        verify(modelMapper).map((Object) any(), (Class<Address>) any());
    }

    @Test
    void testCreateAddress3() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        when(addressRepository.save((Address) any())).thenReturn(address);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Address");
        address1.setState("MD");
        address1.setStreet("Address");
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(address1);
        assertThrows(ResourceException.class, () -> addressServiceImplementation.createAddress(new AddressDto()));
        verify(addressRepository).save((Address) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    @Test
    void testCreateAddress4() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        when(addressRepository.save((Address) any())).thenReturn(address);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(null);
        assertNull(addressServiceImplementation.createAddress(new AddressDto()));
        verify(addressRepository).save((Address) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    @Test
    void testUpdateAddress() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        Optional<Address> ofResult = Optional.of(address);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");
        when(addressRepository.save((Address) any())).thenReturn(address1);
        when(addressRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
        assertThrows(ResourceException.class, () -> addressServiceImplementation.updateAddress(new AddressDto()));
        verify(addressRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Address>) any());
    }

    @Test
    void testUpdateAddress2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        Optional<Address> ofResult = Optional.of(address);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");
        when(addressRepository.save((Address) any())).thenReturn(address1);
        when(addressRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> addressServiceImplementation.updateAddress(new AddressDto()));
        verify(addressRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Address>) any());
    }

    @Test
    void testUpdateAddress3() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        when(addressRepository.save((Address) any())).thenReturn(address);
        when(addressRepository.findById((Long) any())).thenReturn(Optional.empty());
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
        assertThrows(ResourceException.class, () -> addressServiceImplementation.updateAddress(new AddressDto()));
        verify(addressRepository).findById((Long) any());
    }

    @Test
    void testUpdateAddress4() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        Optional<Address> ofResult = Optional.of(address);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");
        when(addressRepository.save((Address) any())).thenReturn(address1);
        when(addressRepository.findById((Long) any())).thenReturn(ofResult);

        Address address2 = new Address();
        address2.setAddressId(123L);
        address2.setCity("Oxford");
        address2.setCountry("GB");
        address2.setPinCode("Address");
        address2.setState("MD");
        address2.setStreet("Address");
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(address2);
        assertThrows(ResourceException.class, () -> addressServiceImplementation.updateAddress(new AddressDto()));
        verify(addressRepository).save((Address) any());
        verify(addressRepository).findById((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    @Test
    void testUpdateAddress5() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        Optional<Address> ofResult = Optional.of(address);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode("Pin Code");
        address1.setState("MD");
        address1.setStreet("Street");
        when(addressRepository.save((Address) any())).thenReturn(address1);
        when(addressRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(null);
        assertNull(addressServiceImplementation.updateAddress(new AddressDto()));
        verify(addressRepository).save((Address) any());
        verify(addressRepository).findById((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    @Test
    void testDeleteAddress() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        Optional<Address> ofResult = Optional.of(address);
        doNothing().when(addressRepository).deleteById((Long) any());
        when(addressRepository.findById((Long) any())).thenReturn(ofResult);
        addressServiceImplementation.deleteAddress(123L);
        verify(addressRepository).findById((Long) any());
        verify(addressRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteAddress2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");
        Optional<Address> ofResult = Optional.of(address);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(addressRepository)
                .deleteById((Long) any());
        when(addressRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> addressServiceImplementation.deleteAddress(123L));
        verify(addressRepository).findById((Long) any());
        verify(addressRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteAddress3() {
        doNothing().when(addressRepository).deleteById((Long) any());
        when(addressRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> addressServiceImplementation.deleteAddress(123L));
        verify(addressRepository).findById((Long) any());
    }
}

