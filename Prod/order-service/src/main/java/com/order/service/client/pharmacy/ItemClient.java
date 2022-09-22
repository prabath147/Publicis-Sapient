package com.order.service.client.pharmacy;

import com.order.service.dto.ItemDto;
import com.order.service.dto.PageableResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@FeignClient(
        name = "item",
        url = "${pharmacyServiceURL}/item"
)
public interface ItemClient {
   
    @GetMapping("/get-item-by-product-id/{id}")
    public ResponseEntity<PageableResponse<ItemDto>> getItemsByProductId(@RequestHeader("Authorization") String bearerToken,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @PathVariable("id") Long productId);
}
