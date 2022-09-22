package com.pharmacy.service.dto;

import com.pharmacy.service.model.Item;
import lombok.*;
import org.hibernate.Hibernate;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

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

    @PositiveOrZero(message = "item quantity cannot be less than 0")
    private Long itemQuantity;
    @PositiveOrZero(message = "price cannot be less than 0")
    private Double price;

    @PastOrPresent(message = "manufacture date cannot exceed today's date")
    private Date manufacturedDate;

    @Future(message = "expiry date should be greater than today's date")
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
