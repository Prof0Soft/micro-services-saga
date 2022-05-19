package com.servicea.servicea.service.impl;

import com.servicea.servicea.dto.OrderDto;
import com.servicea.servicea.dto.ResultDto;
import com.servicea.servicea.dto.TaskStatusDto;
import com.servicea.servicea.entity.Order;
import com.servicea.servicea.entity.Task;
import com.servicea.servicea.repository.OrderRepository;
import com.servicea.servicea.service.OrderService;
import com.servicea.servicea.service.SagaClientService;
import com.servicea.servicea.service.TaskService;
import com.servicea.servicea.type.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Slf4j
@Component
public class TaskExecutor implements com.servicea.servicea.service.TaskExecutor {
    private final OrderService orderService;
    private final TaskService taskService;
    private final SagaClientService sagaClientService;
    private final Random rand = new Random();

    public TaskExecutor(final OrderService orderService,
                        final TaskService taskService,
                        final SagaClientService sagaClientService) {
        this.orderService = orderService;
        this.taskService = taskService;
        this.sagaClientService = sagaClientService;
    }

    @Override
    public void execute(final Task task) {
        log.info("Looking room for task with id: {}", task.getId());
        ResultDto result;
        try {
            if (isCancelled(task.getId())) {
                cancelTask(task);
                return;
            }

            for (int i = 0; i < 100; i += 10) {
                log.info("Task processing..." + i + "%");
                Thread.sleep(2000L);

                if (rand.nextInt(100) < 50) {
                    // imitation of an exceptional situation
                    throw new IllegalArgumentException();
                }
            }

            OrderDto orderDto = new OrderDto();
            orderDto.setTaskId(task.getId());
            orderService.create(orderDto);

            result = taskService.finishTask(task.getId());
        } catch (Exception e) {
            log.error("Error execute task with id: {}", task.getId(), e);
            result = taskService.failTask(task.getId());
        }
        sagaClientService.reply(result);
    }

    private void cancelTask(final Task task) {
        log.info("Task with id: {} is cancelled", task.getId());
        task.setStatus(TaskStatus.CREATED);
    }

    private boolean isCancelled(final UUID taskId) {
        final TaskStatusDto task = taskService.getTaskStatus(taskId);
        return task.getStatus().equals(TaskStatus.CANCELING);
    }
}
