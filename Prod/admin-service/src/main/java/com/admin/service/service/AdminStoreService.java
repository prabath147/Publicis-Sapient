package com.admin.service.service;

import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.StoreDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface AdminStoreService {
    PageableResponse<StoreDto> getAllStores(Integer pageNumber, Integer pageSize);

    StoreDto getStoreByStoreId(Long storeId);

    StoreDto updateStore(StoreDto storeDto);

    ResponseEntity<String> deleteStore(Long storeId);
}
