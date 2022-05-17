package com.servicea.servicea.service.impl;

import com.servicea.servicea.entity.Task;
import com.servicea.servicea.repository.OrderRepository;
import com.servicea.servicea.service.Reverter;
import com.servicea.servicea.type.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author M.Bezmen
 */
@Slf4j
@Component
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
            log.info("Task {} reverted", task.getId());
            log.debug("Task {} reverted", task);
        }
    }
}
