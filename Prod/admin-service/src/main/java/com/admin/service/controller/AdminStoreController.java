package com.admin.service.controller;

import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.StoreDto;
import com.admin.service.service.AdminStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminStoreController {

    @Autowired
    AdminStoreService adminStoreService;

    @GetMapping("/store")
    public ResponseEntity<PageableResponse<StoreDto>> getAllStores(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    ) {

            PageableResponse<StoreDto> storeList = adminStoreService.getAllStores(pageNumber, pageSize);
            return new ResponseEntity<>(storeList, HttpStatus.OK);


    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<StoreDto> getStoreByStoreId(@PathVariable Long storeId) {

            StoreDto store = adminStoreService.getStoreByStoreId(storeId);
            return new ResponseEntity<>(store, HttpStatus.OK);


    }

    @PutMapping("/store/update")
    public ResponseEntity<StoreDto> updateStoreByStoreId(@RequestBody StoreDto storeData) {

            StoreDto updatedStore = adminStoreService.updateStore(storeData);
            return new ResponseEntity<>(updatedStore, HttpStatus.OK);

    }

    @DeleteMapping("/store/{storeId}/delete")
    public ResponseEntity<String> deleteStoreByStoreId(@PathVariable Long storeId) {

            ResponseEntity<String> response = adminStoreService.deleteStore(storeId);
            if (response.getStatusCode() == HttpStatus.OK)
                return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to delete Store " + storeId + " due to some error!!");

    }
}
