package com.adev.gather.service.impl;

import com.adev.common.base.service.impl.BaseServiceImpl;
import com.adev.gather.domain.Order;
import com.adev.gather.repository.OrderRepository;
import com.adev.gather.service.OrderService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }
}
