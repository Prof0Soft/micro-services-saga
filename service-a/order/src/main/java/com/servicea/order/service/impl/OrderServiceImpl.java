package com.servicea.order.service.impl;

import com.servicea.order.dto.OrderDto;
import com.servicea.order.entity.Order;
import com.servicea.order.entity.Task;
import com.servicea.order.mapper.OrderMapper;
import com.servicea.order.repository.OrderRepository;
import com.servicea.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Slf4j
@Component
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(final OrderRepository orderRepository,
                            final OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    @Override
    public OrderDto create(OrderDto orderDto) {

        Task task = new Task();
        task.setId(orderDto.getTaskId());

        Order order = new Order();
        order.setTask(task);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Transactional
    @Override
    public void removeOrderByTaskId(final UUID taskId) {
        orderRepository.removeOrderByAndTaskId(taskId);
    }
}
