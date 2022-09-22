package com.order.service.service;

import com.order.service.dto.OrdersDto;
import com.order.service.dto.PageableResponse;
import com.order.service.model.Orders;

import java.util.List;

public interface OrdersService {
    OrdersDto createOrder(OrdersDto ordersDto);

    OrdersDto getOrder(Long orderId);

    PageableResponse<OrdersDto> getOrders(Long userId, Integer pageNumber, Integer pageSize);

    void deleteOrder(Long orderId);

    void deleteAllOrder(List<Orders> ordersList);

    void deleteAllOrder(String jwt, List<Long> orderIdList);

    void deleteOrderHistory(Long userId);
    
    Long getAllOrders();
}
