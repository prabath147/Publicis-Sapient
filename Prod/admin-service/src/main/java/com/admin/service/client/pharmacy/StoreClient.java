package com.admin.service.client.pharmacy;

import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.StoreDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "store",
        url = "${pharmacyServiceURL}/store"
)
public interface StoreClient {

    @GetMapping(value = "/get-store")
    PageableResponse<StoreDto> getStores(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize);

    @GetMapping(value = "/get-store/{id}")
    StoreDto getStoreById(@PathVariable("id") Long storeId);

    @PostMapping(value = "/create-store")
    StoreDto createStore(@RequestBody StoreDto storeDto);

    @PutMapping(value = "/update-store")
    StoreDto updateStore(@RequestBody StoreDto storeDto);

    @DeleteMapping(value = "/delete-store/{id}")
    ResponseEntity<String> deleteStore(@PathVariable("id") Long storeId);

}
