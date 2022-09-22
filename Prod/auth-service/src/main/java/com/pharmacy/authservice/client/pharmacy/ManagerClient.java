package com.pharmacy.authservice.client.pharmacy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "manager",
        url = "${pharmacyServiceUrl}/manager"
)
public interface ManagerClient {

   @PostMapping("/create-manager/{id}")
   public ResponseEntity<Object> saveManager(@RequestHeader("Authorization") String token, @PathVariable("id") Long userId, @RequestBody Object detailObject);

    @PostMapping("/create-manager/{id}")
    public ResponseEntity<Object> saveManager(@PathVariable("id") Long userId, @RequestBody Object detailObject);

    @DeleteMapping(value = "/delete-manager/{id}")
    public ResponseEntity<String> deleteManager(@PathVariable("id") Long managerId);
}
