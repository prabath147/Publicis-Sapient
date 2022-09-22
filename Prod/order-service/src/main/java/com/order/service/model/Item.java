package com.order.service.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id", unique = true, nullable = false)
    private Long itemId;

    @Column(name = "item_id_fk", nullable = false)
    private Long itemIdFk;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer itemQuantity;
}
