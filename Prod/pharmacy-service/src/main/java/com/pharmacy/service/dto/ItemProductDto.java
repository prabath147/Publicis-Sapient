package com.pharmacy.service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemProductDto {
    private String id;
    private Long itemId;
    private Long productId;
    private Long storeId;
    private String productName;
    private String proprietaryName;
    private Double price;
    private String dosageForm;
    private boolean productType;
    private String imageUrl;
    private Long quantity;
    private String description;
}
