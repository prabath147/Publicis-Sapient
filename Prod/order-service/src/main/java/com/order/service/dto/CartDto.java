package com.order.service.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartDto {
    private Long cartId;

    private Set<ItemDto> items = new HashSet<>();

    @NotNull(message = "quantity cannot be null")
    private Long quantity;

    @NotNull(message = "price cannot be null")
    private Double price;

    public CartDto(Set<ItemDto> items) {
        this.items = items;
        this.quantity = 0L;
        this.price = 0D;
        for (ItemDto itemDto : items) {
            this.quantity += itemDto.getItemQuantity();
            this.price += itemDto.getItemQuantity() * itemDto.getPrice();
        }
    }
}
