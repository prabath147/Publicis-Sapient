package com.pharmacy.service.controller;

import com.pharmacy.service.dto.ItemProductDto;
import com.pharmacy.service.service.implementation.ItemProductServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pharmacy/search")
@CrossOrigin(origins = "*")
public class ItemProductController {
    @Autowired
    private ItemProductServiceImplementation itemProductSearchService;

    @GetMapping("/all-items")
    public ResponseEntity<Page<ItemProductDto>> findAllItemProducts(@RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                    @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                    @RequestParam(name = "sortByPrice", required = false, defaultValue = "asc") String sortByPrice,
                                                                    @RequestParam(name = "productType", required = false) boolean productType) {
        return new ResponseEntity<>(this.itemProductSearchService.findAll(productType, sortByPrice, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/search-items-exact-match")
    public ResponseEntity<Page<ItemProductDto>> exactMatchSearch(@RequestParam(name = "productName", required = false) String productName,
                                                                 @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ResponseEntity<>(this.itemProductSearchService.exactSearch(productName, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/search-items-exact-match/{storeId}")
    public ResponseEntity<Page<ItemProductDto>> exactMatchSearchByStore(@PathVariable("storeId") Long storeId, @RequestParam(name = "productName", required = false) String productName,
                                                                 @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ResponseEntity<>(this.itemProductSearchService.exactSearchByStore(storeId, productName, pageNumber, pageSize), HttpStatus.OK);
    }


    @GetMapping("/items")
    public ResponseEntity<Page<ItemProductDto>> search(@RequestParam(name = "productName", required = false) String productName,
                                                               @RequestParam(name = "productType", required = false) boolean productType,
                                                               @RequestParam(name = "sortByPrice", required = false, defaultValue = "asc") String sortByPrice,
                                                               @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                               @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ResponseEntity<>(this.itemProductSearchService.search(productName, productType, sortByPrice, pageNumber, pageSize), HttpStatus.OK);
    }


    @GetMapping("/item-products-by-store-id/{id}")
    public ResponseEntity<Page<ItemProductDto>> getItemsByStoreId(@PathVariable("id") Long storeId,
                                                            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ResponseEntity<>(this.itemProductSearchService.getItemsByStoreId(storeId, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/get-item-product/{id}")
    public ResponseEntity<ItemProductDto> getItemById(@PathVariable("id") String id) {
        return new ResponseEntity<>(this.itemProductSearchService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/get-suggestions")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam(name = "productName", required = false) String productName) {
        return new ResponseEntity<>(this.itemProductSearchService.getSuggestions(productName), HttpStatus.OK);
    }

}

