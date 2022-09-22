package com.pharmacy.service.service;

import com.pharmacy.service.dto.ItemProductDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ItemProductService {

    Page<ItemProductDto> findAll(boolean productType, String sortByPrice, int pageNumber, int pageSize);
    Page<ItemProductDto> exactSearch(String productName, int pageNumber, int pageSize);
    Page<ItemProductDto> exactSearchByStore(Long storeId, String productName, int pageNumber, int pageSize);
    Page<ItemProductDto> search(String productName, boolean productType, String sortByPrice, int pageNumber, int pageSize);
    ItemProductDto findById(String id);
    Page<ItemProductDto> getItemsByStoreId(Long storeId, int pageNumber, int pageSize);
    List<String> getSuggestions(String keyword);
}

