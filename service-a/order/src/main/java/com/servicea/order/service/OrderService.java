package com.servicea.order.service;

import com.servicea.order.dto.OrderDto;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
public interface OrderService {

    OrderDto create(OrderDto order);

    void removeOrderByTaskId(UUID taskId);
}
