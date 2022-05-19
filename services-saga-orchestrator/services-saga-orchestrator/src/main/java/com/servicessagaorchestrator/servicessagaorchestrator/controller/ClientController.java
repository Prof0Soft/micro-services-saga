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
 * @author Sergey B.
 * 18.05.2022
 */
@RestController
@RequestMapping("/orders")
public class ClientController {

    private final OrderService service;

    public ClientController(final OrderService service) {
        this.service = service;
    }

    /**
     * Initial process.
     *
     * @return
     */
    @PostMapping()
    public OrderDto createOrder() {
        return service.createOrder();
    }

    @GetMapping("/{id}/status")
    public OrderStatusDto getOrderStatus(@PathVariable final UUID id) {
        return service.getOrderStatus(id);
    }

    @PostMapping("/{id}/cancel")
    public OrderStatusDto cancelOrder(@PathVariable final UUID id) {
        return service.cancelOrder(id);
    }

}
