package com.servicea.servicea.service.impl;

import com.servicea.servicea.entity.Task;
import com.servicea.servicea.repository.OrderRepository;
import com.servicea.servicea.service.Reverter;
import com.servicea.servicea.type.TaskStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author M.Bezmen
 */
public class ReverterImpl implements Reverter {
    private final OrderRepository orderRepository;

    public ReverterImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public void revert(final Task task) {
        if (orderRepository.removeOrderByAndTaskId(task.getId())) {
            task.setStatus(TaskStatus.REVERTED);
        }
    }
}
