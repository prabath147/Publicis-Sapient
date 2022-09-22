package com.admin.service.dto;

import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long productId;

    private String proprietaryName;

    private String productName;

    private String description;

    private String dosageForm;

    private Set<CategoryDto> categorySet = new HashSet<>();

    private Long quantity = 0L;

    private String imageUrl;

    private Boolean productType;

    public ProductDto(Long productId, String proprietaryName, String productName, String description, String dosageForm, CategoryDto categoryDto, Long itemQuantity) {
        this.productId = productId;
        this.proprietaryName = proprietaryName;
        this.productName = productName;
        this.description = description;
        this.dosageForm = dosageForm;
        this.categorySet.add(categoryDto);
        this.quantity = itemQuantity;
    }

    public ProductDto(Long productId, String proprietaryName, String productName, String description, String dosageForm, CategoryDto categoryDto) {
        this.productId = productId;
        this.proprietaryName = proprietaryName;
        this.productName = productName;
        this.description = description;
        this.dosageForm = dosageForm;
        this.categorySet.add(categoryDto);
    }

    public ProductDto(Long productId, String proprietaryName, String productName, String description, String dosageForm, CategoryDto categoryDto, Long itemQuantity, String imageUrl) {
        this.productId = productId;
        this.proprietaryName = proprietaryName;
        this.productName = productName;
        this.description = description;
        this.dosageForm = dosageForm;
        this.categorySet.add(categoryDto);
        this.quantity = itemQuantity;
        this.imageUrl = imageUrl;
    }

    public void addCategory(CategoryDto categoryDto) {
        this.categorySet.add(categoryDto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductDto product = (ProductDto) o;
        return productId != null && Objects.equals(productId, product.getProductId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}