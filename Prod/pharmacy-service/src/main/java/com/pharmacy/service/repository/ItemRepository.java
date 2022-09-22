package com.pharmacy.service.repository;

import com.pharmacy.service.model.Item;
import com.pharmacy.service.utils.ItemMini;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<ItemMini> findAllByStore_StoreId(Long storeId, Pageable pageable);

    List<ItemMini> findAllByStore_StoreId(Long storeId);

    Page<ItemMini> findBy(Pageable pageable);

    Page<Item> findAllBy(Pageable pageable);

    List<Item> findAllByExpiryDate(Date expiryDate);

    List<Item> findAllByExpiryDateBefore(Date expiryDate);

    List<Item> findAllByItemQuantityEquals(Long itemQuantity);

    @Async("asyncExecutor")
    CompletableFuture<Integer> countAllByStore_StoreId(Long storeId);

    List<Item> findItemsByStore_StoreId(Long storeId);

    List<Item> findAllByProduct_ProductId(Long productId);

    Page<ItemMini> findAllByProduct_ProductIdOrderByPriceAsc(Long productId, Pageable pageable);


}
