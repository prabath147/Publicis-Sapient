package com.pharmacy.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@ToString
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "proprietary_name", nullable = false)
    private String proprietaryName;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "dosage_form")
    private String dosageForm;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "product_type")
    private boolean productType;        // false = non-generic, true = generic

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @JsonIgnore
    private Set<Category> categorySet = new HashSet<>();

    @Column(name = "quantity")
    private Long quantity = 0L;

    public Product(Long productId, String proprietaryName, String productName, String description, String dosageForm, Category category, Long itemQuantity, boolean productType) {
        this.productId = productId;
        this.proprietaryName = proprietaryName;
        this.productName = productName;
        this.description = description;
        this.dosageForm = dosageForm;
        this.categorySet.add(category);
        this.quantity = itemQuantity;
        this.productType = productType;
    }

    public Product(Long productId, String proprietaryName, String productName, String description, String dosageForm, String imageUrl, boolean productType, Category category, Long itemQuantity) {
        this.productId = productId;
        this.proprietaryName = proprietaryName;
        this.productName = productName;
        this.description = description;
        this.dosageForm = dosageForm;
        this.categorySet.add(category);
        this.quantity = itemQuantity;
        this.productType = productType;
        this.imageUrl = imageUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return productId != null && Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
