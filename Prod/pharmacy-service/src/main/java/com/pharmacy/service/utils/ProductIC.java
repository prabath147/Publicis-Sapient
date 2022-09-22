package com.pharmacy.service.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductIC {
    private Long productId;
    @NotBlank(message = "proprietary name may not be empty or null")
    private String proprietaryName;
    @NotBlank(message = "product name may not be empty or null")
    private String productName;
    @NotBlank(message = "description may not be empty or null")
    private String description;

    private String dosageForm;

    private String categoryName;

    private String imageUrl = "xsl";

    private boolean productType = true;

    public ProductIC(Long productId, String proprietaryName, String productName, String description, String dosageForm, String categoryName) {
        this.productId = productId;
        this.proprietaryName = proprietaryName;
        this.productName = productName;
        this.description = description;
        this.dosageForm = dosageForm;
        this.categoryName = categoryName;
    }
}
