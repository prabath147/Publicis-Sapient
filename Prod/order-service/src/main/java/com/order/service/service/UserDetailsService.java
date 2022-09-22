package com.order.service.service;

import com.order.service.dto.UserDetailsDto;

public interface UserDetailsService {
    UserDetailsDto getUserDetails(Long userId);
    UserDetailsDto createUserDetails(Long userId, UserDetailsDto userDetailsDto);
    UserDetailsDto updateUserDetails(UserDetailsDto userDetailsDto);
    void deleteUserDetails(Long userId);
    boolean checkIfExists(Long userId);
    Long getAllUsers();
}
