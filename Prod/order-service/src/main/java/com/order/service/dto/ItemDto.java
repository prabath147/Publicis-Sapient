package com.order.service.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long itemId;

    private Long itemIdFk;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @NotNull(message = "Item quantity cannot be null")
    private Long itemQuantity;

}
