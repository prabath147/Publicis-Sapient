package com.order.service.controller;

import com.order.service.dto.UserDetailsDto;
import com.order.service.service.UserDetailsService;
import com.order.service.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/order/user-details")
public class UserDetailsController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping(value = "/get-user-details/{id}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only view their own profiles!");
        return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.getUserDetails(userId));
    }

    @PostMapping(value = "/create-user-details/{id}")
    public ResponseEntity<UserDetailsDto> createUserDetails(@PathVariable("id") Long userId,
            @Valid @RequestBody UserDetailsDto userDetailsDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userDetailsService.createUserDetails(userId, userDetailsDto));
    }

    @PostMapping(value = "/update-user-details")
    public ResponseEntity<UserDetailsDto> updateUserDetails(@RequestHeader("Authorization") String jwt,
            @Valid @RequestBody UserDetailsDto userDetailsDto) {
        if (!jwtUtils.verifyId(jwt, userDetailsDto.getUserId(), true))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only update their own profiles!");
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailsService.updateUserDetails(userDetailsDto));
    }

    @GetMapping(value = "/delete-user-details/{id}")
    public ResponseEntity<UserDetailsDto> deleteUserDetails(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only delete their own profiles!");

        userDetailsService.deleteUserDetails(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/get-user-count")
    public ResponseEntity<Long> getAllUsers() {

        return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.getAllUsers());
    }

}
