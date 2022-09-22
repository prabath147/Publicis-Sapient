package com.pharmacy.service.service;

import com.pharmacy.service.dto.ItemDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.model.Item;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public interface ItemService {
    ItemDto getItem(Long itemId);

    PageableResponse<ItemDto> getItemsWithoutStoreInfo(Integer pageNumber, Integer pageSize);

    PageableResponse<ItemDto> getItemsByStore(Long storeId, Integer pageNumber, Integer pageSize);
    PageableResponse<ItemDto> getItemsSortedByStore(Long storeId, Integer pageNumber, Integer pageSize, String keyword, String order);

    Long getTotalItems();

    void deleteExpired() throws ParseException;

    void getNearlyExpired() throws ParseException;

    void getNullInventory();

    List<ItemDto> getStoreItems(Long storeId);

    CompletableFuture<Integer> numberOfItemsInStore(Long storeId);

    List<Item> getItemsByStoreToDelete(Long storeId);

    List<Long> getItemsByProductId(Long productId);

    ItemDto createItem(Long storeId, ItemDto itemDto);

    ItemDto createItem(String jwt, Long storeId, ItemDto itemDto);

    CompletableFuture<Boolean> createItems(List<Item> itemList);

    ItemDto updateItem(ItemDto itemDto);

    ItemDto updateItem(String jwt, ItemDto itemDto);

    void updateItem(List<ItemDto> itemDtoList);

    void deleteItem(Long itemId);

    void deleteItem(String jwt, Long itemId);

    void deleteItems(List<Long> itemIdList);

    PageableResponse<ItemDto> getSortedItemsByProductId(Integer pageNumber, Integer pageSize, Long productId);


    boolean checkIfExists(Long categoryId);
}
