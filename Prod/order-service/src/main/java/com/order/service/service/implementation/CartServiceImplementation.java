package com.order.service.service.implementation;

import com.order.service.dto.CartDto;
import com.order.service.dto.ItemDto;
import com.order.service.exception.ResourceException;
import com.order.service.model.Cart;
import com.order.service.model.Item;
import com.order.service.repository.CartRepository;
import com.order.service.repository.ProductRepository;
import com.order.service.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class CartServiceImplementation implements CartService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CartDto getCart(Long userId) {
        return modelMapper.map(getIfExistsElseCreate(userId), CartDto.class);
    }

    @Override
    public CartDto addToCart(Long userId, ItemDto itemDto) {
        Cart cart = getIfExistsElseCreate(userId);
        cart.add(modelMapper.map(itemDto, Item.class));
        return updateCart(cart);
    }

    @Override
    public CartDto subtractFromCart(Long userId, ItemDto itemDto) {
        Cart cart = getIfExistsElseCreate(userId);
        cart.subtract(modelMapper.map(itemDto, Item.class));
        return updateCart(cart);
    }

    @Override
    public CartDto checkout(Long userId, CartDto cartDto) {
        return updateCart(modelMapper.map(cartDto, Cart.class));
    }

    @Override
    public void checkout(Long userId, Set<ItemDto> itemDtoList) {
        Cart cart = getIfExistsElseCreate(userId);
        for (ItemDto itemDto : itemDtoList) {
            Item item = modelMapper.map(itemDto, Item.class);
            cart.removeItem(item);
            cart.add(item);
        }
        updateCart(cart);
    }

    @Override
    public CartDto addToCart(Long userId, List<ItemDto> itemDtoList) {
        List<Item> itemList = new ArrayList<>();
        for (ItemDto itemDto : itemDtoList) itemList.add(modelMapper.map(itemDto, Item.class));
        Cart cart = getIfExistsElseCreate(userId);
        cart.add(itemList);
        return updateCart(cart);
    }

    @Override
    public CartDto removeFromCart(Long userId, ItemDto itemDto) {
        Cart cart = getIfExistsElseCreate(userId);
        Set<Item> itemSet = cart.getItems();
        Item item = null;
        for(Item item1: itemSet)
            if (Objects.equals(item1.getItemIdFk(), itemDto.getItemIdFk()))
                item = item1;
        itemSet.remove(item);
        cart.setItems(itemSet);
        return updateCart(cart);
    }

    @Override
    public void emptyCartItems(Long userId, Set<ItemDto> itemDtoList) {
        Cart cart = getIfExistsElseCreate(userId);
        Set<Item> itemsToRemove = new HashSet<>();
        Set<Item> itemsInCart = cart.getItems();
        for(Item item: itemsInCart)
            for (ItemDto itemDto: itemDtoList)
                if (Objects.equals(itemDto.getItemIdFk(), item.getItemIdFk()))
                    itemsToRemove.add(item);
        for (Item item: itemsToRemove)
            itemsInCart.remove(item);
        cart.setItems(itemsInCart);
        updateCart(cart);
    }

    @Override
    public CartDto emptyCart(Long userId) {
        Cart cart = getIfExistsElseCreate(userId);
        cart.getItems().clear();
        return updateCart(cart);
    }

    @Override
    public Cart getIfExistsElseCreate(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findById(userId);
        if (optionalCart.isEmpty()) {
            Cart cart = new Cart(userId);
            try {
                return cartRepository.save(cart);
            } catch (Exception e) {
                throw new ResourceException("Cart", "cart", cart, ResourceException.ErrorType.CREATED, e);
            }
        } else {
            return optionalCart.get();
        }
    }

    @Override
    public CartDto reduceItemQuantity(Long userId, ItemDto itemDto) {
        Optional<Cart> optionalCart = cartRepository.findById(userId);
        Item item1 = modelMapper.map(itemDto, Item.class);
        if (optionalCart.isPresent()) {
            Set<Item> items = optionalCart.get().getItems();
            Cart cart = optionalCart.get();
            for (Item item : items) {
                if (item.getItemIdFk().equals(item1.getItemIdFk())) {
                    items.remove(item);
                    item.setItemQuantity(item.getItemQuantity() - item1.getItemQuantity());
                    if (item.getItemQuantity() > 0) {
                        items.add(item);
                    }
                    cart.setItems(items);
                    return modelMapper.map(cartRepository.save(cart), CartDto.class);
                }
            }
        }
        return null;
    }

    @Override
    public void deleteCart(Long userId) {
        try {
            Cart cart = getIfExistsElseCreate(userId);
            cart.getItems().clear();
            cartRepository.save(cart);
            cartRepository.deleteById(userId);
        } catch (Exception e) {
            throw new ResourceException("Cart", "ID", userId, ResourceException.ErrorType.DELETED, e);
        }

    }

    private CartDto updateCart(Cart cart) {
        try {
            cart.doCalc();
            return modelMapper.map(cartRepository.save(cart), CartDto.class);
        } catch (Exception e) {
            throw new ResourceException("Cart", "cart", cart, ResourceException.ErrorType.UPDATED, e);
        }
    }

}
