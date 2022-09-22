package com.pharmacy.service.controller;

import com.pharmacy.service.dto.ItemDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.ProductDto;
import com.pharmacy.service.service.ItemService;
import com.pharmacy.service.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/pharmacy/item")
@CrossOrigin(origins = "*")
@Slf4j
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ProductService productService;

    @GetMapping("/get-item")
    public ResponseEntity<PageableResponse<ItemDto>> getItems(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return new ResponseEntity<>(this.itemService.getItemsWithoutStoreInfo(pageNumber, pageSize), HttpStatus.OK);
    }

    // NEW
    @GetMapping(value = "/total-items")
    public ResponseEntity<Long> getTotalItems() {
        return ResponseEntity.ok().body(itemService.getTotalItems());
    }

    @GetMapping("/get-item/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Long itemId) {
        return new ResponseEntity<>(this.itemService.getItem(itemId), HttpStatus.OK);
    }

    @PostMapping(value = "/create-item/{id}")
    public ResponseEntity<ItemDto> createItem(@RequestHeader("Authorization") String jwt,
                                              @PathVariable("id") Long storeId, @Valid @RequestBody ItemDto itemDto) {
        if (!productService.checkIfExists(itemDto.getProduct().getProductId())) {
            ProductDto productDto = itemDto.getProduct();
            productService.createProduct(productDto);
            productService.updateQuantity(productDto.getProductId(), itemDto.getItemQuantity());
        } else {
            itemDto.setProduct(productService.getProduct(itemDto.getProduct().getProductId()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(jwt, storeId, itemDto));
    }

    @PutMapping(value = "/update-item")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader("Authorization") String jwt,
                                              @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.updateItem(jwt, itemDto));
    }

    @DeleteMapping(value = "/delete-item/{id}")
    public ResponseEntity<String> deleteItem(@RequestHeader("Authorization") String jwt,
                                             @PathVariable("id") Long itemId) {
        itemService.deleteItem(jwt, itemId);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Item Successfully!");
    }

    @GetMapping("/get-item-by-product-id/{id}")
    public ResponseEntity<PageableResponse<ItemDto>> getItemsByProductId(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @PathVariable("id") Long productId) {
        return new ResponseEntity<>(itemService.getSortedItemsByProductId(pageNumber, pageSize, productId),
                HttpStatus.OK);
    }
}