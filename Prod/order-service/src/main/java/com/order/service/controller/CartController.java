package com.order.service.controller;

import com.order.service.client.pharmacy.StoreClient;
import com.order.service.dto.CartDto;
import com.order.service.dto.ItemDto;
import com.order.service.exception.ResourceException;
import com.order.service.service.CartService;
import com.order.service.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/order/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private StoreClient storeClient;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping(value = "/get-cart/{id}")
    public ResponseEntity<CartDto> getCart(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId) {
    	if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only view their own carts!");
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart(userId));

    }

    @PostMapping("/checkout-cart/{id}")
    public ResponseEntity<CartDto> placeOrder(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId, @Valid @RequestBody CartDto cartDto) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only checkout their own carts!");
        try {
            CartDto cartDto1 = storeClient.checkout(cartDto).getBody();
            return ResponseEntity.status(HttpStatus.CREATED).body(cartService.checkout(userId, cartDto1));
        } catch (Exception e) {
            throw new ResourceException("Cart", "ID", userId, ResourceException.ErrorType.VERIFIED, e);
        }
    }

    @PostMapping("/checkout-items/{id}")
    public ResponseEntity<CartDto> placeOrder(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId, @Valid @RequestBody Set<ItemDto> itemDtoList) {
    	 if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only checkout their own carts!");
        try {
            CartDto cartDto1 = storeClient.checkout(new CartDto(itemDtoList)).getBody();
            assert cartDto1 != null;
            cartService.checkout(userId, cartDto1.getItems());
            return ResponseEntity.status(HttpStatus.CREATED).body(cartDto1);
        } catch (Exception e) {
            throw new ResourceException("Cart", "ID", userId, ResourceException.ErrorType.VERIFIED, e);
        }
    }

    @PostMapping("/add-to-cart/{id}")
    public ResponseEntity<CartDto> addToCart(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only add items to their own carts!");
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addToCart(userId, itemDto));
    }

    @PostMapping("/add-item/{id}")
    public ResponseEntity<Boolean> addItemToCart(@RequestHeader("Authorization") String jwt,
                                             @PathVariable("id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only add items to their own carts!");
        cartService.addToCart(userId, itemDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/subtract-from-cart/{id}")
    public ResponseEntity<CartDto> subtractFromCart(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only remove items from their own carts!");
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.subtractFromCart(userId, itemDto));
    }

    @PostMapping("/subtract-item/{id}")
    public ResponseEntity<Boolean> subtractItemFromCart(@RequestHeader("Authorization") String jwt,
                                                    @PathVariable("id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only remove items from their own carts!");
        cartService.subtractFromCart(userId, itemDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/add-prescription-to-cart/{id}")
    public ResponseEntity<CartDto> addPrescriptionToCart(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId, @Valid @RequestBody List<ItemDto> itemDtoList) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only add prescriptions their own carts!");
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addToCart(userId, itemDtoList));
    }

    @PutMapping("/remove-from-cart/{id}")
    public ResponseEntity<CartDto> deleteItemFromCart(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Customers can only remove items their own carts!");
        return ResponseEntity.status(HttpStatus.OK).body(cartService.removeFromCart(userId, itemDto));
    }

    @DeleteMapping("/empty-cart/{id}")
    public ResponseEntity<CartDto> emptyCart(@RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long userId) {
        if (!jwtUtils.verifyId(jwt, userId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can only empty their own carts!");
        return ResponseEntity.status(HttpStatus.OK).body(cartService.emptyCart(userId));
    }

}
