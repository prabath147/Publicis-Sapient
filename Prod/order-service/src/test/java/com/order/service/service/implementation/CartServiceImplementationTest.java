package com.order.service.service.implementation;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.order.service.dto.CartDto;
import com.order.service.dto.ItemDto;
import com.order.service.exception.ResourceException;
import com.order.service.model.Cart;
import com.order.service.model.Item;
import com.order.service.repository.CartRepository;
import com.order.service.repository.ProductRepository;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CartServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class CartServiceImplementationTest {
    @MockBean
    private CartRepository cartRepository;

    @Autowired
    private CartServiceImplementation cartServiceImplementation;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void testGetCart() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.getCart(123L));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testGetCart2() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.getCart(123L));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testGetCart3() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.getCart(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testGetCart4() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.getCart(123L));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testGetCart5() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.getCart(123L));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testGetCart6() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.getCart(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testAddToCart() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setPrice(10.0d);
        item2.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ItemDto()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    @Test
    void testAddToCart2() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Item>) any()))
                .thenThrow(new ResourceException("Cart", "Cart", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testAddToCart3() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).add((Item) any());
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setPrice(10.0d);
        item2.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ItemDto()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).add((Item) any());
        verify(cart1).doCalc();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    @Test
    void testAddToCart4() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).add((Item) any());
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setPrice(10.0d);
        item2.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ItemDto()));
        verify(cartRepository, atLeast(1)).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    @Test
    void testAddToCart5() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.addToCart(123L, new ArrayList<>()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testAddToCart6() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ArrayList<>()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testAddToCart7() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).add((List<Item>) any());
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.addToCart(123L, new ArrayList<>()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).add((List<Item>) any());
        verify(cart1).doCalc();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testAddToCart8() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).add((List<Item>) any());
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.addToCart(123L, new ArrayList<>()));
        verify(cartRepository, atLeast(1)).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testAddToCart9() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setItemQuantity(42);
        item2.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ItemDto()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    
    @Test
    void testAddToCart10() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Item>) any()))
                .thenThrow(new ResourceException("Cart", "Cart", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testAddToCart11() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).add((Item) any());
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setItemQuantity(42);
        item2.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ItemDto()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).add((Item) any());
        verify(cart1).doCalc();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    
    @Test
    void testAddToCart12() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).add((Item) any());
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setItemQuantity(42);
        item2.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ItemDto()));
        verify(cartRepository, atLeast(1)).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    @Test
    void testAddToCart13() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.addToCart(123L, new ArrayList<>()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testAddToCart14() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.addToCart(123L, new ArrayList<>()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testAddToCart15() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).add((List<Item>) any());
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.addToCart(123L, new ArrayList<>()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).add((List<Item>) any());
        verify(cart1).doCalc();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testAddToCart16() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).add((List<Item>) any());
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.addToCart(123L, new ArrayList<>()));
        verify(cartRepository, atLeast(1)).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testSubtractFromCart2() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Item>) any()))
                .thenThrow(new ResourceException("Item", "Item", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.subtractFromCart(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testSubtractFromCart3() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        doNothing().when(cart1).subtract((Item) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setItemQuantity(42);
        item2.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.subtractFromCart(123L, new ItemDto()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).doCalc();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(cart1).subtract((Item) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    
    @Test
    void testRemoveFromCart() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.removeFromCart(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testRemoveFromCart2() {
        Item item = mock(Item.class);
        doNothing().when(item).setItemId((Long) any());
        doNothing().when(item).setItemIdFk((Long) any());
        doNothing().when(item).setPrice((Double) any());
        doNothing().when(item).setItemQuantity((Integer) any());
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);
        Cart cart = mock(Cart.class);
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Item item1 = mock(Item.class);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setPrice((Double) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setPrice(10.0d);
        item2.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.removeFromCart(123L, new ItemDto()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item).setItemId((Long) any());
        verify(item).setItemIdFk((Long) any());
        verify(item).setPrice((Double) any());
        verify(item).setItemQuantity((Integer) any());
        verify(cart1).doCalc();
        verify(cart1, atLeast(1)).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setPrice((Double) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testRemoveFromCart3() {
        Item item = mock(Item.class);
        doNothing().when(item).setItemId((Long) any());
        doNothing().when(item).setItemIdFk((Long) any());
        doNothing().when(item).setPrice((Double) any());
        doNothing().when(item).setItemQuantity((Integer) any());
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);
        Cart cart = mock(Cart.class);
        doNothing().when(cart).doCalc();
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());
        Item item1 = mock(Item.class);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setPrice((Double) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setPrice(10.0d);
        item2.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.removeFromCart(123L, new ItemDto()));
        verify(cartRepository, atLeast(1)).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart).doCalc();
        verify(cart, atLeast(1)).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item).setItemId((Long) any());
        verify(item).setItemIdFk((Long) any());
        verify(item).setPrice((Double) any());
        verify(item).setItemQuantity((Integer) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setPrice((Double) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Item>) any());
    }

    
    @Test
    void testRemoveFromCart4() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.removeFromCart(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

   
    @Test
    void testRemoveFromCart5() {
        Item item = mock(Item.class);
        doNothing().when(item).setItemId((Long) any());
        doNothing().when(item).setItemIdFk((Long) any());
        doNothing().when(item).setItemQuantity((Integer) any());
        doNothing().when(item).setPrice((Double) any());
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);
        Cart cart = mock(Cart.class);
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Item item1 = mock(Item.class);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        doNothing().when(item1).setPrice((Double) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setItemQuantity(42);
        item2.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.removeFromCart(123L, new ItemDto()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item).setItemId((Long) any());
        verify(item).setItemIdFk((Long) any());
        verify(item).setItemQuantity((Integer) any());
        verify(item).setPrice((Double) any());
        verify(cart1).doCalc();
        verify(cart1, atLeast(1)).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(item1).setPrice((Double) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Item>) any());
    }

   
    @Test
    void testRemoveFromCart6() {
        Item item = mock(Item.class);
        doNothing().when(item).setItemId((Long) any());
        doNothing().when(item).setItemIdFk((Long) any());
        doNothing().when(item).setItemQuantity((Integer) any());
        doNothing().when(item).setPrice((Double) any());
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);
        Cart cart = mock(Cart.class);
        doNothing().when(cart).doCalc();
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());
        Item item1 = mock(Item.class);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        doNothing().when(item1).setPrice((Double) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);

        Item item2 = new Item();
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setItemQuantity(42);
        item2.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.removeFromCart(123L, new ItemDto()));
        verify(cartRepository, atLeast(1)).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart).doCalc();
        verify(cart, atLeast(1)).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item).setItemId((Long) any());
        verify(item).setItemIdFk((Long) any());
        verify(item).setItemQuantity((Integer) any());
        verify(item).setPrice((Double) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(item1).setPrice((Double) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testEmptyCart() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.emptyCart(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testEmptyCart2() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.emptyCart(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testEmptyCart3() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).emptyCart();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.emptyCart(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).doCalc();
        verify(cart1).emptyCart();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testEmptyCart4() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).emptyCart();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.emptyCart(123L));
        verify(cartRepository, atLeast(1)).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testEmptyCart5() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.emptyCart(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testEmptyCart6() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.emptyCart(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

   
    @Test
    void testEmptyCart7() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).emptyCart();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.emptyCart(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).doCalc();
        verify(cart1).emptyCart();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testEmptyCart8() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).emptyCart();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        CartDto cartDto = new CartDto();
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(cartDto);
        assertSame(cartDto, cartServiceImplementation.emptyCart(123L));
        verify(cartRepository, atLeast(1)).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testGetIfExistsElseCreate() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(cart1, cartServiceImplementation.getIfExistsElseCreate(123L));
        verify(cartRepository).findById((Long) any());
    }


    @Test
    void testGetIfExistsElseCreate2() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertSame(cart, cartServiceImplementation.getIfExistsElseCreate(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
    }

    @Test
    void testGetIfExistsElseCreate3() {
    	Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
       // when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());
        when(cartRepository.save((Cart) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
//        when(cartRepository.findById((Long) any())).thenThrow(
//                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.getIfExistsElseCreate(123L));
        verify(cartRepository).findById((Long) any());
    }

   
    @Test
    void testGetIfExistsElseCreate4() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(cart1, cartServiceImplementation.getIfExistsElseCreate(123L));
        verify(cartRepository).findById((Long) any());
    }

    
    @Test
    void testGetIfExistsElseCreate5() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertSame(cart, cartServiceImplementation.getIfExistsElseCreate(123L));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
    }

    @Test
    void testReduceItemQuantity() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item1);
        assertNull(cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testReduceItemQuantity2() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testReduceItemQuantity3() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);
        Cart cart = mock(Cart.class);
        when(cart.getItems()).thenReturn(new HashSet<>());
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item1);
        assertNull(cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(cart).getItems();
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testReduceItemQuantity4() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);
        Item item1 = mock(Item.class);
        when(item1.getItemQuantity()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(item1.getItemIdFk()).thenReturn(42L);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setPrice((Double) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        HashSet<Item> itemSet = new HashSet<>();
        itemSet.add(item1);
        Cart cart = mock(Cart.class);
        when(cart.getItems()).thenReturn(itemSet);
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        Item item2 = mock(Item.class);
        doNothing().when(item2).setItemId((Long) any());
        doNothing().when(item2).setItemIdFk((Long) any());
        doNothing().when(item2).setPrice((Double) any());
        doNothing().when(item2).setItemQuantity((Integer) any());
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setPrice(10.0d);
        item2.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item2);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart1);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item3 = new Item();
        item3.setItemId(123L);
        item3.setItemIdFk(42L);
        item3.setPrice(10.0d);
        item3.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item3);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item2).setItemId((Long) any());
        verify(item2).setItemIdFk((Long) any());
        verify(item2).setPrice((Double) any());
        verify(item2).setItemQuantity((Integer) any());
        verify(cart).getItems();
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item1).getItemQuantity();
        verify(item1).getItemIdFk();
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setPrice((Double) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testReduceItemQuantity5() {
        Item item = mock(Item.class);
        doNothing().when(item).setItemId((Long) any());
        doNothing().when(item).setItemIdFk((Long) any());
        doNothing().when(item).setPrice((Double) any());
        doNothing().when(item).setItemQuantity((Integer) any());
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);
        Cart cart = mock(Cart.class);
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());
        Item item1 = mock(Item.class);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setPrice((Double) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Item item2 = mock(Item.class);
        when(item2.getItemQuantity()).thenReturn(1);
        when(item2.getItemIdFk()).thenReturn(42L);
        doNothing().when(item2).setItemId((Long) any());
        doNothing().when(item2).setItemIdFk((Long) any());
        doNothing().when(item2).setPrice((Double) any());
        doNothing().when(item2).setItemQuantity((Integer) any());
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setPrice(10.0d);
        item2.setItemQuantity(1);

        HashSet<Item> itemSet = new HashSet<>();
        itemSet.add(item2);
        Cart cart1 = mock(Cart.class);
        when(cart1.getItems()).thenReturn(itemSet);
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);

        Item item3 = new Item();
        item3.setItemId(123L);
        item3.setItemIdFk(42L);
        item3.setPrice(10.0d);
        item3.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item3);
        assertNull(cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item).setItemId((Long) any());
        verify(item).setItemIdFk((Long) any());
        verify(item).setPrice((Double) any());
        verify(item).setItemQuantity((Integer) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item2).setItemId((Long) any());
        verify(item2).setItemIdFk((Long) any());
        verify(item2).setPrice((Double) any());
        verify(item2).setItemQuantity((Integer) any());
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setPrice((Double) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }


    @Test
    void testReduceItemQuantity6() {
        Item item = mock(Item.class);
        doNothing().when(item).setItemId((Long) any());
        doNothing().when(item).setItemIdFk((Long) any());
        doNothing().when(item).setPrice((Double) any());
        doNothing().when(item).setItemQuantity((Integer) any());
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);
        Item item1 = mock(Item.class);
        when(item1.getItemQuantity()).thenReturn(1);
        when(item1.getItemIdFk()).thenReturn(42L);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setPrice((Double) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        HashSet<Item> itemSet = new HashSet<>();
        itemSet.add(item1);
        Cart cart = mock(Cart.class);
        when(cart.getItems()).thenReturn(itemSet);
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        Item item2 = mock(Item.class);
        doNothing().when(item2).setItemId((Long) any());
        doNothing().when(item2).setItemIdFk((Long) any());
        doNothing().when(item2).setPrice((Double) any());
        doNothing().when(item2).setItemQuantity((Integer) any());
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setPrice(10.0d);
        item2.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item2);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart1);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        Item item3 = mock(Item.class);
        when(item3.getItemQuantity()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(item3.getItemIdFk()).thenReturn(42L);
        doNothing().when(item3).setItemId((Long) any());
        doNothing().when(item3).setItemIdFk((Long) any());
        doNothing().when(item3).setPrice((Double) any());
        doNothing().when(item3).setItemQuantity((Integer) any());
        item3.setItemId(123L);
        item3.setItemIdFk(42L);
        item3.setPrice(10.0d);
        item3.setItemQuantity(1);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item3);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item2).setItemId((Long) any());
        verify(item2).setItemIdFk((Long) any());
        verify(item2).setPrice((Double) any());
        verify(item2).setItemQuantity((Integer) any());
        verify(cart).getItems();
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item1).getItemQuantity();
        verify(item1).getItemIdFk();
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setPrice((Double) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(item).setItemId((Long) any());
        verify(item).setItemIdFk((Long) any());
        verify(item).setPrice((Double) any());
        verify(item).setItemQuantity((Integer) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
        verify(item3).getItemQuantity();
        verify(item3).getItemIdFk();
        verify(item3).setItemId((Long) any());
        verify(item3).setItemIdFk((Long) any());
        verify(item3).setPrice((Double) any());
        verify(item3).setItemQuantity((Integer) any());
    }

   
    @Test
    void testReduceItemQuantity7() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item1);
        assertNull(cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

   
    @Test
    void testReduceItemQuantity8() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

   
    @Test
    void testReduceItemQuantity9() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);
        Cart cart = mock(Cart.class);
        when(cart.getItems()).thenReturn(new HashSet<>());
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item1);
        assertNull(cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(cart).getItems();
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    
    @Test
    void testReduceItemQuantity10() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);
        Item item1 = mock(Item.class);
        when(item1.getItemQuantity()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(item1.getItemIdFk()).thenReturn(42L);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        doNothing().when(item1).setPrice((Double) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        HashSet<Item> itemSet = new HashSet<>();
        itemSet.add(item1);
        Cart cart = mock(Cart.class);
        when(cart.getItems()).thenReturn(itemSet);
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        Item item2 = mock(Item.class);
        doNothing().when(item2).setItemId((Long) any());
        doNothing().when(item2).setItemIdFk((Long) any());
        doNothing().when(item2).setItemQuantity((Integer) any());
        doNothing().when(item2).setPrice((Double) any());
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setItemQuantity(42);
        item2.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item2);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart1);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);

        Item item3 = new Item();
        item3.setItemId(123L);
        item3.setItemIdFk(42L);
        item3.setItemQuantity(42);
        item3.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item3);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item2).setItemId((Long) any());
        verify(item2).setItemIdFk((Long) any());
        verify(item2).setItemQuantity((Integer) any());
        verify(item2).setPrice((Double) any());
        verify(cart).getItems();
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item1).getItemQuantity();
        verify(item1).getItemIdFk();
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(item1).setPrice((Double) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    
    @Test
    void testReduceItemQuantity11() {
        Item item = mock(Item.class);
        doNothing().when(item).setItemId((Long) any());
        doNothing().when(item).setItemIdFk((Long) any());
        doNothing().when(item).setItemQuantity((Integer) any());
        doNothing().when(item).setPrice((Double) any());
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);
        Cart cart = mock(Cart.class);
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());
        Item item1 = mock(Item.class);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        doNothing().when(item1).setPrice((Double) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Item item2 = mock(Item.class);
        when(item2.getItemQuantity()).thenReturn(42);
        when(item2.getItemIdFk()).thenReturn(42L);
        doNothing().when(item2).setItemId((Long) any());
        doNothing().when(item2).setItemIdFk((Long) any());
        doNothing().when(item2).setItemQuantity((Integer) any());
        doNothing().when(item2).setPrice((Double) any());
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setItemQuantity(42);
        item2.setPrice(10.0d);

        HashSet<Item> itemSet = new HashSet<>();
        itemSet.add(item2);
        Cart cart1 = mock(Cart.class);
        when(cart1.getItems()).thenReturn(itemSet);
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);

        Item item3 = new Item();
        item3.setItemId(123L);
        item3.setItemIdFk(42L);
        item3.setItemQuantity(42);
        item3.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item3);
        assertNull(cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item).setItemId((Long) any());
        verify(item).setItemIdFk((Long) any());
        verify(item).setItemQuantity((Integer) any());
        verify(item).setPrice((Double) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item2).setItemId((Long) any());
        verify(item2).setItemIdFk((Long) any());
        verify(item2).setItemQuantity((Integer) any());
        verify(item2).setPrice((Double) any());
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(item1).setPrice((Double) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testReduceItemQuantity12() {
        Item item = mock(Item.class);
        doNothing().when(item).setItemId((Long) any());
        doNothing().when(item).setItemIdFk((Long) any());
        doNothing().when(item).setItemQuantity((Integer) any());
        doNothing().when(item).setPrice((Double) any());
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);
        Item item1 = mock(Item.class);
        when(item1.getItemQuantity()).thenReturn(42);
        when(item1.getItemIdFk()).thenReturn(42L);
        doNothing().when(item1).setItemId((Long) any());
        doNothing().when(item1).setItemIdFk((Long) any());
        doNothing().when(item1).setItemQuantity((Integer) any());
        doNothing().when(item1).setPrice((Double) any());
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        HashSet<Item> itemSet = new HashSet<>();
        itemSet.add(item1);
        Cart cart = mock(Cart.class);
        when(cart.getItems()).thenReturn(itemSet);
        doNothing().when(cart).removeItem((Item) any());
        doNothing().when(cart).setCartId((Long) any());
        doNothing().when(cart).setItems((Set<Item>) any());
        doNothing().when(cart).setPrice((Double) any());
        doNothing().when(cart).setQuantity((Long) any());
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart);
        Item item2 = mock(Item.class);
        doNothing().when(item2).setItemId((Long) any());
        doNothing().when(item2).setItemIdFk((Long) any());
        doNothing().when(item2).setItemQuantity((Integer) any());
        doNothing().when(item2).setPrice((Double) any());
        item2.setItemId(123L);
        item2.setItemIdFk(42L);
        item2.setItemQuantity(42);
        item2.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item2);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart1);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        Item item3 = mock(Item.class);
        when(item3.getItemQuantity()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(item3.getItemIdFk()).thenReturn(42L);
        doNothing().when(item3).setItemId((Long) any());
        doNothing().when(item3).setItemIdFk((Long) any());
        doNothing().when(item3).setItemQuantity((Integer) any());
        doNothing().when(item3).setPrice((Double) any());
        item3.setItemId(123L);
        item3.setItemIdFk(42L);
        item3.setItemQuantity(42);
        item3.setPrice(10.0d);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item3);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.reduceItemQuantity(123L, new ItemDto()));
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(item2).setItemId((Long) any());
        verify(item2).setItemIdFk((Long) any());
        verify(item2).setItemQuantity((Integer) any());
        verify(item2).setPrice((Double) any());
        verify(cart).getItems();
        verify(cart).removeItem((Item) any());
        verify(cart).setCartId((Long) any());
        verify(cart).setItems((Set<Item>) any());
        verify(cart).setPrice((Double) any());
        verify(cart).setQuantity((Long) any());
        verify(item1).getItemQuantity();
        verify(item1).getItemIdFk();
        verify(item1).setItemId((Long) any());
        verify(item1).setItemIdFk((Long) any());
        verify(item1).setItemQuantity((Integer) any());
        verify(item1).setPrice((Double) any());
        verify(item).setItemId((Long) any());
        verify(item).setItemIdFk((Long) any());
        verify(item).setItemQuantity((Integer) any());
        verify(item).setPrice((Double) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
        verify(item3).getItemQuantity();
        verify(item3).getItemIdFk();
        verify(item3).setItemId((Long) any());
        verify(item3).setItemIdFk((Long) any());
        verify(item3).setItemQuantity((Integer) any());
        verify(item3).setPrice((Double) any());
    }

    @Test
    void testDeleteCart() {
        doNothing().when(cartRepository).deleteById((Long) any());
        cartServiceImplementation.deleteCart(123L);
        verify(cartRepository).deleteById((Long) any());
    }

    
    @Test
    void testDeleteCart2() {
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(cartRepository)
                .deleteById((Long) any());
        assertThrows(ResourceException.class, () -> cartServiceImplementation.deleteCart(123L));
        verify(cartRepository).deleteById((Long) any());
    }



    @Test
    void testCheckout() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Cart>) any())).thenReturn(cart1);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.checkout(123L, new CartDto()));
        verify(cartRepository).save((Cart) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    @Test
    void testCheckout2() {
        when(cartRepository.save((Cart) any()))
                .thenThrow(new ResourceException("Cart", "Cart", "Field Value", ResourceException.ErrorType.CREATED));

        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Cart>) any())).thenReturn(cart);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.checkout(123L, new CartDto()));
        verify(cartRepository).save((Cart) any());
        verify(modelMapper).map((Object) any(), (Class<Cart>) any());
    }
    
    @Test
    void testCheckout12() {
       ItemDto item = new ItemDto();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42L);
        item.setPrice(10.0d);

        

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart = new Cart();
        cart.removeItem(item1);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        
        Set<ItemDto> itemList=new HashSet<>();
        itemList.add(item);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
       // when(cartServiceImplementation.getIfExistsElseCreate(123L)).thenReturn(cart1);
        
        when(modelMapper.map(item, Item.class)).thenReturn(item1);
        
       // when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(new CartDto());
        cartServiceImplementation.checkout(123L, itemList);
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
      //  verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testCheckout3() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setPrice(10.0d);
        item.setItemQuantity(1);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setPrice(10.0d);
        item1.setItemQuantity(1);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Cart>) any())).thenReturn(cart1);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.checkout(123L, new CartDto()));
        verify(cartRepository).save((Cart) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(cart1).doCalc();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
    }

    @Test
    void testCheckout4() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Cart>) any())).thenReturn(cart1);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.checkout(123L, new CartDto()));
        verify(cartRepository).save((Cart) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    
    @Test
    void testCheckout5() {
        when(cartRepository.save((Cart) any()))
                .thenThrow(new ResourceException("Cart", "Cart", "Field Value", ResourceException.ErrorType.CREATED));

        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Cart>) any())).thenReturn(cart);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.checkout(123L, new CartDto()));
        verify(cartRepository).save((Cart) any());
        verify(modelMapper).map((Object) any(), (Class<Cart>) any());
    }

    @Test
    void testCheckout6() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<Cart>) any())).thenReturn(cart1);
        assertThrows(ResourceException.class, () -> cartServiceImplementation.checkout(123L, new CartDto()));
        verify(cartRepository).save((Cart) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(cart1).doCalc();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
    }

    @Test
    void testCheckout7() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(new CartDto());
        cartServiceImplementation.checkout(123L, new HashSet<>());
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    @Test
    void testCheckout8() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);

        Cart cart1 = new Cart();
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> cartServiceImplementation.checkout(123L, new HashSet<>()));
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testCheckout9() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        Optional<Cart> ofResult = Optional.of(cart1);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(new CartDto());
        cartServiceImplementation.checkout(123L, new HashSet<>());
        verify(cartRepository).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).doCalc();
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }

    
    @Test
    void testCheckout10() {
        Item item = new Item();
        item.setItemId(123L);
        item.setItemIdFk(42L);
        item.setItemQuantity(42);
        item.setPrice(10.0d);

        Cart cart = new Cart();
        cart.removeItem(item);
        cart.setCartId(123L);
        cart.setItems(new HashSet<>());
        cart.setPrice(10.0d);
        cart.setQuantity(1L);
        when(cartRepository.save((Cart) any())).thenReturn(cart);
        when(cartRepository.findById((Long) any())).thenReturn(Optional.empty());

        Item item1 = new Item();
        item1.setItemId(123L);
        item1.setItemIdFk(42L);
        item1.setItemQuantity(42);
        item1.setPrice(10.0d);
        Cart cart1 = mock(Cart.class);
        doNothing().when(cart1).doCalc();
        doNothing().when(cart1).removeItem((Item) any());
        doNothing().when(cart1).setCartId((Long) any());
        doNothing().when(cart1).setItems((Set<Item>) any());
        doNothing().when(cart1).setPrice((Double) any());
        doNothing().when(cart1).setQuantity((Long) any());
        cart1.removeItem(item1);
        cart1.setCartId(123L);
        cart1.setItems(new HashSet<>());
        cart1.setPrice(10.0d);
        cart1.setQuantity(1L);
        when(modelMapper.map((Object) any(), (Class<CartDto>) any())).thenReturn(new CartDto());
        cartServiceImplementation.checkout(123L, new HashSet<>());
        verify(cartRepository, atLeast(1)).save((Cart) any());
        verify(cartRepository).findById((Long) any());
        verify(cart1).removeItem((Item) any());
        verify(cart1).setCartId((Long) any());
        verify(cart1).setItems((Set<Item>) any());
        verify(cart1).setPrice((Double) any());
        verify(cart1).setQuantity((Long) any());
        verify(modelMapper).map((Object) any(), (Class<CartDto>) any());
    }
}

