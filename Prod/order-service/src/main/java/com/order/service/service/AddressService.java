package com.order.service.service;

import com.order.service.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto getAddress(Long addressId);
    AddressDto createAddress(AddressDto addressDto);
    AddressDto updateAddress(AddressDto addressDto);
    void deleteAddress(Long addressId);
}

