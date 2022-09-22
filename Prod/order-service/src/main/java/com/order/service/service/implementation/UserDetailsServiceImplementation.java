package com.order.service.service.implementation;

import com.order.service.dto.UserDetailsDto;
import com.order.service.exception.ResourceException;
import com.order.service.model.Address;
import com.order.service.model.UserDetails;
import com.order.service.repository.OrdersRepository;
import com.order.service.repository.RepeatOrderRepository;
import com.order.service.repository.UserDetailsRepository;
import com.order.service.service.CartService;
import com.order.service.service.OrdersService;
import com.order.service.service.RepeatOrderService;
import com.order.service.service.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private RepeatOrderService repeatOrderService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartService cartService;

    @Override
    public UserDetailsDto getUserDetails(Long userId) {
        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findById(userId);
        if (optionalUserDetails.isEmpty())
            throw new ResourceException("UserDetails", "ID", userId, ResourceException.ErrorType.FOUND);
        return modelMapper.map(optionalUserDetails.get(), UserDetailsDto.class);
    }

    @Override
    public UserDetailsDto createUserDetails(Long userId, UserDetailsDto userDetailsDto) {
        userDetailsDto.setUserId(userId);
        Address address = modelMapper.map(userDetailsDto.getAddress(), Address.class);
        UserDetails userDetails = modelMapper.map(userDetailsDto, UserDetails.class);
        userDetails.setAddress(address);
        cartService.getIfExistsElseCreate(userId);
        try {
            return modelMapper.map(userDetailsRepository.save(userDetails), UserDetailsDto.class);
        } catch (Exception e) {
            throw new ResourceException("UserDetails", "user details", userDetails, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public UserDetailsDto updateUserDetails(UserDetailsDto userDetailsDto) {
        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findById(userDetailsDto.getUserId());
        if (optionalUserDetails.isEmpty())
            throw new ResourceException("UserDetails", "user details", userDetailsDto, ResourceException.ErrorType.FOUND);
        try {
            return modelMapper.map(userDetailsRepository.save(optionalUserDetails.get()), UserDetailsDto.class);
        } catch (Exception e) {
            throw new ResourceException("UserDetails", "user details", userDetailsDto, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    public void deleteUserDetails(Long userId) {
        if (!userDetailsRepository.existsById(userId))
            throw new ResourceException("UserDetails", "ID", userId, ResourceException.ErrorType.FOUND);
        try {
            cartService.deleteCart(userId);
            ordersService.deleteOrderHistory(userId);
            repeatOrderService.deleteOptInHistory(userId);
            userDetailsRepository.deleteById(userId);
        } catch (Exception e) {
            throw new ResourceException("UserDetails", "ID", userId, ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public boolean checkIfExists(Long userId) {
        return userDetailsRepository.existsById(userId);
    }

	@Override
	public Long getAllUsers() {
		return userDetailsRepository.count();
	}
}
