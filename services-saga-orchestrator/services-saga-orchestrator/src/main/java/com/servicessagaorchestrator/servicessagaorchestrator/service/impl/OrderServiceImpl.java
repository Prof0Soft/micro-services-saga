package com.servicessagaorchestrator.servicessagaorchestrator.service.impl;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.OrderStatusDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.BookingFlow;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import com.servicessagaorchestrator.servicessagaorchestrator.exception.BadRequestException;
import com.servicessagaorchestrator.servicessagaorchestrator.exception.NotFoundException;
import com.servicessagaorchestrator.servicessagaorchestrator.mapper.OrderMapper;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.OrderRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.SagaProcessRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.service.OrderService;
import com.servicessagaorchestrator.servicessagaorchestrator.service.SagaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Slf4j
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

    /**
     * Create order.
     *
     * @return {@link OrderDto }
     */
    @Transactional
    @Override
    public OrderDto createOrder() {
        log.info("Start create order process");

        final Order orderEntity = new Order();
        orderEntity.setStatus(TaskStatus.NEW);

        log.debug("Created orderEntity {}", orderEntity);

        final SagaProcess sagaProcess = new SagaProcess();
        sagaProcess.setOrder(orderEntity);
        sagaProcess.setSteps(BookingFlow.buildFlow());

        log.debug("Created sagaProcess {}", sagaProcess);


        final SagaProcess saved = sagaProcessRepository.save(sagaProcess);

        sagaService.initSaga(saved);

        return orderMapper.toDto(saved.getOrder());
    }

    /**
     * Get order of status by id.
     *
     * @param id {@link UUID}
     * @return {@link OrderStatusDto} with info about order with id.
     * @throw NotFoundException if order not found.
     */
    @Override
    public OrderStatusDto getOrderStatus(final UUID id) {
        log.debug("Get order status init for id {}", id);
        final Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isEmpty()) {
            log.error("Order with id {} is empty", id);
            throw new NotFoundException(id.toString());
        }

        return orderMapper.toStatusDto(optionalOrder.get());
    }

    /**
     * Cancel order by id.
     *
     * @param id {link UUID} of order.
     * @return {@link OrderStatusDto} with info about order with id.
     * @throw NotFoundException if order not found.
     * @throw BadRequestException if order not running.
     */
    @Transactional
    @Override
    public OrderStatusDto cancelOrder(final UUID id) {
        log.info("Cancel order for id {}", id);

        if (id == null) {
            log.warn("Id can't been null.");
            throw new BadRequestException();
        }

        final Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isEmpty()) {
            log.warn("Can't found order with id {}", id);
            throw new NotFoundException(id.toString());
        }

        if (optionalOrder.get().getStatus() != TaskStatus.RUNNING) {
            log.warn("Bad request. The status is not RUNNING.");
            throw new BadRequestException(id.toString());
        }

        sagaService.cancelSaga(id);

        return getOrderStatus(id);
    }
}
