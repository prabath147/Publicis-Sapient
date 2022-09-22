package com.pharmacy.service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "search", shards = 3, replicas = 2)
public class ItemProduct {
    @Id
    private String id;
    @Field(type = FieldType.Long, name = "item_id")
    private Long itemId;
    @Field(type = FieldType.Long, name = "product_id")
    private Long productId;
    @Field(type = FieldType.Long, name = "store_id")
    private Long storeId;
    @Field(type = FieldType.Text, name = "product_name")
    private String productName;
    @Field(type = FieldType.Text, name = "proprietary_name")
    private String proprietaryName;
    @Field(type = FieldType.Double, name = "price")
    private Double price;
    @Field(type = FieldType.Text, name = "dosage_form")
    private String dosageForm;
    @Field(type = FieldType.Boolean, name = "product_type")
    private boolean productType;
    @Field(type = FieldType.Text, name = "image_url")
    private String imageUrl;
    @Field(type = FieldType.Long, name = "quantity")
    private Long quantity;
    @Field(type = FieldType.Text, name = "description")
    private String description;
}
