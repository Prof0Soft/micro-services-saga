package com.servicessagaorchestrator.servicessagaorchestrator.service.impl;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderStatusDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.BookingFlow;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import com.servicessagaorchestrator.servicessagaorchestrator.exception.NotFoundException;
import com.servicessagaorchestrator.servicessagaorchestrator.mapper.OrderMapper;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.OrderRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.SagaProcessRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.service.OrderService;
import com.servicessagaorchestrator.servicessagaorchestrator.service.SagaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final SagaService sagaService;
    private final SagaProcessRepository sagaProcessRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(final SagaProcessRepository sagaProcessRepository,
                            final OrderMapper orderMapper,
                            final SagaService sagaService, final OrderRepository orderRepository) {
        this.sagaProcessRepository = sagaProcessRepository;
        this.orderMapper = orderMapper;
        this.sagaService = sagaService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public OrderDto createOrder() {
        final Order orderEntity = new Order();
        orderEntity.setStatus(TaskStatus.NEW);
        final SagaProcess sagaProcess = new SagaProcess();
        sagaProcess.setOrder(orderEntity);
        sagaProcess.setSteps(BookingFlow.buildFlow());

        final SagaProcess saved = sagaProcessRepository.save(sagaProcess);

        sagaService.initSaga(saved);

        return orderMapper.toDto(saved.getOrder());
    }

    @Override
    public OrderStatusDto getOrderStatus(final String id) {
        final Optional<Order> optionalOrder = orderRepository.findById(UUID.fromString(id));
        if (optionalOrder.isEmpty()) {
            throw new NotFoundException(id);
        }
        return orderMapper.toStatusDto(optionalOrder.get());
    }

    @Transactional
    @Override
    public OrderStatusDto cancelOrder(final String id) {
        sagaService.cancelSaga(id);
        return getOrderStatus(id);
    }
}
