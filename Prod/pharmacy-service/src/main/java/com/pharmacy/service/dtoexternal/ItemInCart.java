package com.pharmacy.service.dtoexternal;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemInCart {
    Long itemId;
    Long itemIdFk;
    Long itemQuantity;
    Double price;
}
