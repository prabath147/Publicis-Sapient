package com.pharmacy.service.service;

import com.pharmacy.service.dto.AddressDto;
import org.springframework.stereotype.Component;


@Component
public interface AddressService {
    AddressDto getAddress(Long addressId);
    AddressDto createAddress(AddressDto addressDto);
    AddressDto updateAddress(AddressDto addressDto);
    void deleteAddress(Long addressId);
}
