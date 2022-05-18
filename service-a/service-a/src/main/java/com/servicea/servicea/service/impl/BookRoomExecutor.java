package com.servicea.servicea.service.impl;

import com.servicea.servicea.dto.ResultDto;
import com.servicea.servicea.entity.Order;
import com.servicea.servicea.entity.Task;
import com.servicea.servicea.repository.OrderRepository;
import com.servicea.servicea.repository.TaskRepository;
import com.servicea.servicea.service.TaskExecutor;
import com.servicea.servicea.service.client.SagaClient;
import com.servicea.servicea.type.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Slf4j
@Component
public class BookRoomExecutor implements TaskExecutor {
    private final OrderRepository orderRepository;
    private final TaskRepository taskRepository;
    private final SagaClient client;

    public BookRoomExecutor(final OrderRepository orderRepository,
                            final TaskRepository taskRepository,
                            final SagaClient client) {
        this.orderRepository = orderRepository;
        this.taskRepository = taskRepository;
        this.client = client;
    }

    @Transactional
    @Override
    public void execute(final Task task) {
        log.info("Looking room for task with id: {}", task.getId());
        try {
            if (isCancelled(task.getId())) {
                cancelTask(task);
                return;
            }
            final Order order = new Order();
            order.setTask(task);
            task.setStatus(TaskStatus.DONE);
            taskRepository.save(task);
            orderRepository.save(order);
        } catch (Exception e) {
            log.error("Error execute task with id: {}", task.getId(), e);
            task.setStatus(TaskStatus.FAILED);
            taskRepository.save(task);
        } finally {
            final ResultDto resultDto = new ResultDto();
            resultDto.setTaskId(task.getId().toString());
            resultDto.setStatus(task.getStatus());
            resultDto.setServiceName(task.getServiceName());
            client.sendResult(resultDto);
        }
    }

    private void cancelTask(final Task task) {
        log.info("Task with id: {} is cancelled", task.getId());
        task.setStatus(TaskStatus.CREATED);
    }

    private boolean isCancelled(final UUID taskId) {
        final Optional<Task> task = taskRepository.findById(taskId);
        return task.isPresent() && task.get().getStatus().equals(TaskStatus.CANCELING);
    }
}
