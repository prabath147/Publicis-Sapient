package com.order.service.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class OrdersDto {

    private Long orderId;

    private Long userId;

    @NotEmpty(message = "Order should have atleast 1 item")
    private Set<ItemDto> items = new HashSet<>();

    @PositiveOrZero(message = "Quantity should be greater than or equal to zero")
    private Long quantity = 0L;

    @PositiveOrZero(message = "Price cannot be negative")
    private Double price = 0D;

    @Valid
    private OrderDetailsDto orderDetails;

    @Valid
    private AddressDto orderAddress;

    @PastOrPresent
    private Date orderDate = new Date();

    @PastOrPresent
    private Date deliveryDate = Date.from(LocalDate.now().plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());

    private boolean optionalOrderDetails;

    public OrdersDto(Long userId, Set<ItemDto> itemDtoSet, OrderDetailsDto orderDetails, AddressDto orderAddress, boolean optionalOrderDetails, Date deliveryDate) {
        this.userId = userId;
        this.orderDetails = orderDetails;
        this.orderAddress = orderAddress;
        this.optionalOrderDetails = optionalOrderDetails;
        this.orderDate = new Date();
        this.deliveryDate = deliveryDate;
        this.items = itemDtoSet;
        this.price = 0D;
        this.quantity = 0L;
    }
}
