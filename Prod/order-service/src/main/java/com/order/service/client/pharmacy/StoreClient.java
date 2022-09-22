package com.order.service.client.pharmacy;

import com.order.service.dto.CartDto;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@FeignClient(
        name = "store",
        url = "${pharmacyServiceURL}/store"
)
public interface StoreClient {
    @PostMapping(value = "/buy-cart")
    public ResponseEntity<CartDto> buyCart(@RequestBody CartDto cartDto);

    @PostMapping(value = "/checkout")
    public ResponseEntity<CartDto> checkout(@RequestBody CartDto cartDto);
    
    @PostMapping(value = "/buy-cart")
    public ResponseEntity<CartDto> buyCart(@RequestHeader("Authorization") String bearerToken,@RequestBody CartDto cartDto);
    
}
