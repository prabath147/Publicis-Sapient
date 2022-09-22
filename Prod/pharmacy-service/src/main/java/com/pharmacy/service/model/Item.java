package com.pharmacy.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@ToString
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id", nullable = false)
    private Long itemId;

   @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
   @OnDelete(action = OnDeleteAction.CASCADE)
   @JoinColumn(name="store_id",referencedColumnName = "store_id", nullable=false)
   @JsonIgnore
   @ToString.Exclude
   private Store store;

    @ManyToOne(optional = false, fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @JsonIgnore
    @ToString.Exclude
    private Product product;

    @Column(name = "quantity")
    private Long itemQuantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "manufatured_date")
    @Temporal(TemporalType.DATE)
    private Date manufacturedDate;

    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

//    @Column(name = "image_url")
//    private String imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return itemId != null && Objects.equals(itemId, item.itemId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
