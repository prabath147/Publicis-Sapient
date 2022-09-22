package com.admin.service.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDto {
    private Long itemId;

    @ToString.Exclude
    private StoreDto store;

    private ProductDto product;

    private Long itemQuantity;

    private Double price;

    private Date manufacturedDate;

    private Date expiryDate;

    public ItemDto(Long itemId, ProductDto product, Long itemQuantity, Double price, Date manufacturedDate, Date expiryDate) {
        this.itemId = itemId;
        this.product = product;
        this.itemQuantity = itemQuantity;
        this.price = price;
        this.manufacturedDate = manufacturedDate;
        this.expiryDate = expiryDate;
    }

    public ItemDto(ProductDto product, Long itemQuantity, Double price, Date manufacturedDate, Date expiryDate, StoreDto store) {
        this.product = product;
        this.itemQuantity = itemQuantity;
        this.price = price;
        this.manufacturedDate = manufacturedDate;
        this.expiryDate = expiryDate;
        this.store = store;
    }
}

