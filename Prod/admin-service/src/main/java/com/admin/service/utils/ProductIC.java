package com.admin.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductIC {
    private Long productId;

    private String proprietaryName;

    private String productName;

    private String description;

    private String dosageForm;

    private String categoryName;

    private Long quantity;

    private String imageUrl;

    public ProductIC(Long productId, String proprietaryName, String productName, String description, String dosageForm, String categoryName, Long quantity) {
        this.productId = productId;
        this.proprietaryName = proprietaryName;
        this.productName = productName;
        this.description = description;
        this.dosageForm = dosageForm;
        this.categoryName = categoryName;
        this.quantity = quantity;
    }
}
