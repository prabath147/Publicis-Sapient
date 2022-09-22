package com.order.service.controller;

import com.order.service.dto.PageableResponse;
import com.order.service.dto.RepeatOrderDto;
import com.order.service.service.RepeatOrderService;
import com.order.service.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
public class RepeatOrderController {

    @Autowired
    private RepeatOrderService repeatOrderService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/optin/{id}")
    public ResponseEntity<RepeatOrderDto> getOptInById(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long optInId) {
        if (!jwtUtils.verifyId(jwt, repeatOrderService.getOptInById(optInId).getUserId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only get their own OptIn details!");
        return ResponseEntity.status(HttpStatus.OK).body(repeatOrderService.getOptInById(optInId));
    }

    @GetMapping("/optin/user/{id}")
    public ResponseEntity<PageableResponse<RepeatOrderDto>> getOptInByUserId(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only get their own OptIn history!");
        return ResponseEntity.status(HttpStatus.OK)
                .body(repeatOrderService.getAllOptInByUserId(userId, pageNumber, pageSize));
    }

    @PostMapping("/optin/create-optin")
    public ResponseEntity<RepeatOrderDto> createOptIn(@RequestHeader("Authorization") String jwt,
            @Valid @RequestBody RepeatOrderDto repeatOrderRequest) {
        if (!jwtUtils.verifyId(jwt, repeatOrderRequest.getUserId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only set their own OptIn details!");
        return ResponseEntity.status(HttpStatus.CREATED).body(repeatOrderService.createOptIn(repeatOrderRequest));
    }

    @PutMapping("/optin/update-optin")
    public ResponseEntity<RepeatOrderDto> updateOptIn(@RequestHeader("Authorization") String jwt,
            @Valid @RequestBody RepeatOrderDto repeatOrderRequest) {
        if (!jwtUtils.verifyId(jwt, repeatOrderRequest.getUserId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only updated their own OptIn details!");
        return ResponseEntity.status(HttpStatus.OK).body(repeatOrderService.updateOptIn(repeatOrderRequest));
    }

    @DeleteMapping(value = "/optin/delete/{id}")
    public ResponseEntity<String> deleteOptInById(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long optInId) {
        if (!jwtUtils.verifyId(jwt, repeatOrderService.getOptInById(optInId).getUserId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only delete their own OptIn details!");
        repeatOrderService.deleteOptIn(optInId);
        return ResponseEntity.status(HttpStatus.OK).body("Optin Order Deleted Successfully!");
    }

}
