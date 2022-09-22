package com.order.service.service;

import com.order.service.dto.CartDto;
import com.order.service.dto.ItemDto;
import com.order.service.model.Cart;

import java.util.List;
import java.util.Set;


public interface CartService {

    CartDto getCart(Long userId);

    CartDto addToCart(Long userId, ItemDto itemDto);

    CartDto subtractFromCart(Long userId, ItemDto itemDto);

    CartDto checkout(Long userId, CartDto cartDto);

    void checkout(Long userId, Set<ItemDto> itemDtoList);

    CartDto addToCart(Long userId, List<ItemDto> itemDtoList);

    CartDto removeFromCart(Long userId, ItemDto itemDto);

    void emptyCartItems(Long userId, Set<ItemDto> itemDtoList);

    CartDto emptyCart(Long userId);

    Cart getIfExistsElseCreate(Long userId);

    CartDto reduceItemQuantity(Long userId, ItemDto itemDto);

    void deleteCart(Long userId);

}
