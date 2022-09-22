package com.admin.service.service.impl;

import com.admin.service.client.pharmacy.StoreClient;
import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.StoreDto;
import com.admin.service.exception.ResourceException;
import com.admin.service.service.AdminStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class AdminStoreServiceImpl implements AdminStoreService {

    @Value("${pharmacyServiceURL}")
    private String pharmacyServiceURL;


    @Autowired
    private StoreClient storeClient;


    @Override
    public PageableResponse<StoreDto> getAllStores(Integer pageNumber, Integer pageSize) {
        try
        {
            return storeClient.getStores(pageNumber, pageSize);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Internal Server Error");
        }
    }

    @Override
    public StoreDto getStoreByStoreId(Long storeId) {
        try
        {
            return storeClient.getStoreById(storeId);
        }
        catch (Exception e)
        {
            throw new ResourceException("Store", "storeId", storeId,ResourceException.ErrorType.FOUND);
        }


    }

    @Override
    public StoreDto updateStore(StoreDto storeDto) {
        try
        {
            return storeClient.updateStore(storeDto);
        }
        catch (Exception e)
        {
            throw new ResourceException("Store", "store", storeDto, ResourceException.ErrorType.UPDATED);
        }

    }

    @Override
    public ResponseEntity<String> deleteStore(Long storeId) {

        try
        {
            return storeClient.deleteStore(storeId);
        }
        catch (Exception e)
        {
            throw new ResourceException("Store", "storeId", storeId,ResourceException.ErrorType.FOUND);
        }

    }
}
