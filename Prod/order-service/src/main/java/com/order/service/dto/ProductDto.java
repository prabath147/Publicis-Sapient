package com.order.service.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long productId;

    private Long productIdFk;

    @PositiveOrZero(message = "Quantity cannot be negative")
    private Long quantity;

    @PositiveOrZero(message = "Price cannot be negative")
    private Double price;

}
