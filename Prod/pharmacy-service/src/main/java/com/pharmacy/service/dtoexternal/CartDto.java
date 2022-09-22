package com.pharmacy.service.dtoexternal;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartDto {
    private Set<ItemInCart> items = new HashSet<>();
    private Long cartId;
    @PositiveOrZero(message = "quantity cannot be less than 0")
    private Long quantity;
    @PositiveOrZero(message = "price cannot be less than 0")
    private Double price;
}
