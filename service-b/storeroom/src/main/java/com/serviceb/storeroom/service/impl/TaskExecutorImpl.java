package com.serviceb.storeroom.service.impl;

import com.serviceb.storeroom.dto.ItemReservationDto;
import com.serviceb.storeroom.dto.ResultDto;
import com.serviceb.storeroom.dto.TaskStatusDto;
import com.serviceb.storeroom.entity.Task;
import com.serviceb.storeroom.exception.TaskNotRunningException;
import com.serviceb.storeroom.service.ItemReservationService;
import com.serviceb.storeroom.service.SagaClientService;
import com.serviceb.storeroom.service.TaskExecutor;
import com.serviceb.storeroom.service.TaskService;
import com.serviceb.storeroom.type.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Slf4j
@Component
public class TaskExecutorImpl implements TaskExecutor {
    private static final String SERVICE_NAME = "Service_B";
    private final ItemReservationService itemReservationService;
    private final TaskService taskService;
    private final SagaClientService sagaClientService;
    private final Random rand = new Random();

    public TaskExecutorImpl(final ItemReservationService itemReservationService,
                            final TaskService taskService,
                            final SagaClientService sagaClientService) {
        this.itemReservationService = itemReservationService;
        this.taskService = taskService;
        this.sagaClientService = sagaClientService;
    }

    @Override
    public void execute(final Task task) {
        log.info("Looking room for task with id: {}", task.getId());
        ResultDto result;
        try {
            if (isCancelled(task.getId())) {
                throw new TaskNotRunningException(task.getId());
            }

            for (int i = 0; i < 100; i += 10) {
                log.info("Task processing..." + i + "%");
                Thread.sleep(1000L);

                if (rand.nextInt(100) + 1 < 2) {
                    throw new IllegalArgumentException("imitation of an exceptional situation");
                }
            }

            ItemReservationDto orderDto = new ItemReservationDto();
            orderDto.setTaskId(task.getId());
            itemReservationService.create(orderDto);

            result = taskService.finishTask(task.getId());
        } catch (TaskNotRunningException ex) {
            log.warn("Task was canceled.");
            result = new ResultDto(task.getId(), SERVICE_NAME, TaskStatus.CANCELED);
            taskService.updateTaskStatusById(task.getId(), TaskStatus.CANCELED);

        } catch (Exception e) {
            log.error("Error execute task with id: {}", task.getId(), e);
            result = taskService.failTask(task.getId());
        }

        sagaClientService.reply(result);
    }

    private boolean isCancelled(final UUID taskId) {
        final TaskStatusDto task = taskService.getTaskStatus(taskId);
        return task.getStatus().equals(TaskStatus.CANCELING);
    }
}
