package com.pharmacy.service.repository;

import com.pharmacy.service.model.ItemProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemProductRepository extends ElasticsearchRepository<ItemProduct, String> {
    Page<ItemProduct> findAllByProductType(boolean productType, Pageable pageable);
    Page<ItemProduct> findItemProductByProductNameContainingIgnoreCaseAndProductType(String productName, boolean productType, Pageable pageable);
    Page<ItemProduct> findAllByProductNameAndProductType(String productName, boolean productType, Pageable pageable);
    Page<ItemProduct> findAllByStoreId(Long storeId, Pageable pageable);
    Page<ItemProduct> findAllByProductNameAndStoreId(String productName, Long storeId, Pageable pageable);
}