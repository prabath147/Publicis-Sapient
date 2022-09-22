package com.order.service.model;

import com.order.service.exception.ResourceException;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cart {
    @Id
    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Item> items = new HashSet<>();

    @Column(name = "quantity")
    private Long quantity = 0L;

    @Column(name = "price")
    private Double price = 0D;


    public Cart(Long cartId) {
        this.cartId = cartId;
        this.price = 0D;
        this.items = new HashSet<>();
        this.quantity = 0L;
    }

    public void subtract(Item item) {
        Optional<Item> optionalItem = this.items.stream().filter(p -> Objects.equals(p.getItemIdFk(), item.getItemIdFk())).findAny();
        if (optionalItem.isPresent()) {
            Item i = optionalItem.get();
            this.items.remove(i);
            if (i.getItemQuantity() > item.getItemQuantity()) {
                i.setItemQuantity(i.getItemQuantity() - item.getItemQuantity());
                this.items.add(i);
            }
        } else throw new ResourceException("Item", "item", item, ResourceException.ErrorType.FOUND);
    }

    public void add(Item item) {
        Optional<Item> optionalItem = this.items.stream().filter(p -> Objects.equals(p.getItemIdFk(), item.getItemIdFk())).findAny();
        if (optionalItem.isEmpty()) this.items.add(item);
        else {
            Item i = optionalItem.get();
            this.items.remove(i);
            i.setItemQuantity(i.getItemQuantity() + item.getItemQuantity());
            this.items.add(i);
        }
    }

    public void add(List<Item> itemList) {
        for (Item item : itemList) this.add(item);
    }

    public void doCalc() {
        this.price = 0D;
        this.quantity = 0L;
        for (Item item : this.items) {
            this.price += item.getItemQuantity() * item.getPrice();
            this.quantity += item.getItemQuantity();
        }
    }

    public void removeItem(Item item) {
        this.items = items.stream().filter(i -> !Objects.equals(item.getItemIdFk(), i.getItemIdFk())).collect(Collectors.toSet());

    }

    public void emptyCart() {
        this.items = new HashSet<>();
        this.price = 0D;
        this.quantity = 0L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Cart cart = (Cart) o;
        return cartId != null && Objects.equals(cartId, cart.cartId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
