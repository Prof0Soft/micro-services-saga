package com.servicea.servicea.service;

import com.servicea.servicea.dto.OrderDto;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
public interface OrderService {

    OrderDto create(OrderDto order);

    void removeOrderByTaskId(UUID taskId);
}
