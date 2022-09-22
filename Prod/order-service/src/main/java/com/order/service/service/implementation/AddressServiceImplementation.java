package com.order.service.service.implementation;

import com.order.service.dto.AddressDto;
import com.order.service.exception.ResourceException;
import com.order.service.model.Address;
import com.order.service.repository.AddressRepository;
import com.order.service.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImplementation implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDto getAddress(Long addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (optionalAddress.isEmpty())
            throw new ResourceException("Address", "ID", addressId, ResourceException.ErrorType.FOUND);
        return modelMapper.map(optionalAddress.get(), AddressDto.class);

    }

    @Override
    public AddressDto createAddress(AddressDto addressDto) {
        try {
            return modelMapper.map(addressRepository.save(modelMapper.map(addressDto, Address.class)), AddressDto.class);
        } catch (Exception e) {
            throw new ResourceException("Address", "address", addressDto, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public AddressDto updateAddress(AddressDto addressDto) {
        Optional<Address> optionalAddress = addressRepository.findById(addressDto.getAddressId());
        if (optionalAddress.isEmpty())
            throw new ResourceException("Address", "address", addressDto, ResourceException.ErrorType.FOUND);
        try {
            return modelMapper.map(addressRepository.save(modelMapper.map(addressDto, Address.class)), AddressDto.class);
        } catch (Exception e) {
            throw new ResourceException("Address", "address", addressDto, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    public void deleteAddress(Long addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (optionalAddress.isEmpty())
            throw new ResourceException("Address", "ID", addressId, ResourceException.ErrorType.FOUND);
        try {
            addressRepository.deleteById(addressId);
        } catch (Exception e) {
            throw new ResourceException("Address", "ID", addressId, ResourceException.ErrorType.DELETED, e);
        }
    }
}
