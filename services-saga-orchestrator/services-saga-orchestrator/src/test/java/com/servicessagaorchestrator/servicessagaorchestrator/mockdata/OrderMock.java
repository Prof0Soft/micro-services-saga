package com.servicessagaorchestrator.servicessagaorchestrator.mockdata;

import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;

import java.util.UUID;

/**
 * @author Sergey B.
 * 19.05.2022
 */
public class OrderMock {
    public static Order createOrder() {
        final Order order = new Order();
        order.setStatus(TaskStatus.NEW);
        order.setId(UUID.randomUUID());

        return order;
    }

    public static Order createOrder(final TaskStatus status) {
        final Order order = new Order();
        order.setStatus(status);
        order.setId(UUID.randomUUID());

        return order;
    }
}
