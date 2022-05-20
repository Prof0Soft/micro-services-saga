package com.servicessagaorchestrator.servicessagaorchestrator.controller;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderStatusDto;
import com.servicessagaorchestrator.servicessagaorchestrator.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Client controller for orchestrator control.
 *
 * @author Sergey B.
 * 18.05.2022
 */
@RestController
@RequestMapping("/orders")
public class ClientController {

    private final OrderService orderService;

    public ClientController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public OrderDto createOrder() {
        return orderService.createOrder();
    }

    @GetMapping("/{id}/status")
    public OrderStatusDto getOrderStatus(@PathVariable final UUID id) {
        return orderService.getOrderStatus(id);
    }

    @PostMapping("/{id}/cancel")
    public OrderStatusDto cancelOrder(@PathVariable final String id) {
        return orderService.cancelOrder(UUID.fromString(id));
    }

}