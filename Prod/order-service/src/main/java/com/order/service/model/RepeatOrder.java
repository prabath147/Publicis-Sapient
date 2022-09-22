package com.order.service.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepeatOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "repeat_order_id", nullable = false)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "number_of_deliveries")
    private int numberOfDeliveries;

    @Column(name = "interval_in_days")
    private int intervalInDays;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id ")
    @ToString.Exclude
    private Address address;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Product> repeatOrderItems = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RepeatOrder repeatOrder = (RepeatOrder) o;
        return id != null && Objects.equals(id, repeatOrder.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
