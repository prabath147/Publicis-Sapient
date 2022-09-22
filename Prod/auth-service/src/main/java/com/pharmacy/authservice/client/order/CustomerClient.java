package com.pharmacy.authservice.client.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "order",
        url = "${orderServiceUrl}/user-details"
)
public interface CustomerClient {

    @PostMapping(value = "/create-user-details/{id}")
    public ResponseEntity<Object> createUserDetails(@RequestHeader("Authorization") String token, @PathVariable("id") Long userId, @RequestBody Object detailObject);

    @PostMapping(value = "/create-user-details/{id}")
    public ResponseEntity<Object> createUserDetails(@PathVariable("id") Long userId, @RequestBody Object detailObject);

    @DeleteMapping(value = "/delete-user-details/{id}")
    public ResponseEntity<String> deleteUserDetails(@PathVariable("id") Long userId);

}
