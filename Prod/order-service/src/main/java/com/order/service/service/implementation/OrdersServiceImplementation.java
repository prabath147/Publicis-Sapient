package com.order.service.service.implementation;

import com.order.service.dto.OrdersDto;
import com.order.service.dto.PageableResponse;
import com.order.service.exception.ResourceException;
import com.order.service.model.Orders;
import com.order.service.repository.OrdersRepository;
import com.order.service.service.OrdersService;
import com.order.service.utils.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersServiceImplementation implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public OrdersDto createOrder(OrdersDto ordersDto) {
        try {
            return modelMapper.map(ordersRepository.save(modelMapper.map(ordersDto, Orders.class)), OrdersDto.class);
        } catch (Exception e) {
            throw new ResourceException("Order", "order", ordersDto, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public OrdersDto getOrder(Long orderId) {
        Optional<Orders> optionalOrders = ordersRepository.findById(orderId);
        if (optionalOrders.isEmpty())
            throw new ResourceException("Order", "ID", orderId, ResourceException.ErrorType.FOUND);
        return modelMapper.map(optionalOrders.get(), OrdersDto.class);
    }

    @Override
    public PageableResponse<OrdersDto> getOrders(Long userId, Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<Orders> ordersPage = ordersRepository.findAllByUserId(userId, requestedPage);
        List<OrdersDto> ordersDtoList = new ArrayList<>();
        for (Orders orders : ordersPage.getContent())
            ordersDtoList.add(modelMapper.map(orders, OrdersDto.class));
        PageableResponse<OrdersDto> pageableOrdersResponse = new PageableResponse<>();
        return pageableOrdersResponse.setResponseData(ordersDtoList, ordersPage);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Optional<Orders> optionalOrders = ordersRepository.findById(orderId);
        if (optionalOrders.isEmpty())
            throw new ResourceException("Order", "ID", orderId, ResourceException.ErrorType.FOUND);
        try {
            ordersRepository.deleteById(orderId);
        } catch (Exception e) {
            throw new ResourceException("Order", "ID", orderId, ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public void deleteAllOrder(List<Orders> ordersList) {
        try {
            ordersRepository.deleteAll(ordersList);
        } catch (Exception e) {
            throw new ResourceException("Orders", "order list", ordersList, ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public void deleteAllOrder(String jwt, List<Long> orderIdList) {
        List<Orders> ordersList = ordersRepository.findAllByOrderIdIn(orderIdList);
        for (Orders orders : ordersList)
            if (!jwtUtils.verifyId(jwt, orders.getUserId(), false))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Users can only set their own order details!");
        deleteAllOrder(ordersList);
    }

    @Override
    public void deleteOrderHistory(Long userId) {
        List<Orders> ordersList = ordersRepository.findAllByUserIdEquals(userId);
        try {
            ordersRepository.deleteAll(ordersList);
        } catch (Exception e) {
            throw new ResourceException("Orders", "order list", ordersList, ResourceException.ErrorType.DELETED, e);
        }
    }

	@Override
	public Long getAllOrders() {
		
		return ordersRepository.count();
	}
}
