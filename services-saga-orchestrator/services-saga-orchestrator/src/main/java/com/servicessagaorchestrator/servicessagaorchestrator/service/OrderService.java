package com.servicessagaorchestrator.servicessagaorchestrator.service;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderStatusDto;

/**
 * @author Sergey B.
 * 18.05.2022
 */
public interface OrderService {
    OrderDto createOrder();

    OrderStatusDto getOrderStatus(String id);

    OrderStatusDto cancelOrder(String id);
}
