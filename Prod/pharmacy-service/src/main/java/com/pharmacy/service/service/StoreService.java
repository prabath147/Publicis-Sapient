package com.pharmacy.service.service;

import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.StoreDto;
import com.pharmacy.service.model.Store;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface StoreService {
    StoreDto getStore(Long storeId);
    Long noOfStores(Long managerId);
    Long noOfStores();
    Double totalRevenue(Long managerId);
    void getStoreAsync(Long storeId);
    PageableResponse<StoreDto> getStores(Integer pageNumber, Integer pageSize);
    boolean checkIfExists(Long storeId);
    PageableResponse<StoreDto> getManagerStore(Long managerId, Integer pageNumber, Integer pageSize);
    List<Store> getStoresByManagerToDelete(Long managerId);
    StoreDto createStore(StoreDto storeDto);
    StoreDto updateStore(StoreDto storeDto);
    void updateStore(Set<StoreDto> storeDtoList);
    void deleteStore(Long storeId);
}
