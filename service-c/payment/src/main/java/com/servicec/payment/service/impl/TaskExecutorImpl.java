package com.servicec.payment.service.impl;

import com.servicec.payment.dto.PaymentDto;
import com.servicec.payment.dto.ResultDto;
import com.servicec.payment.dto.TaskStatusDto;
import com.servicec.payment.entity.Task;
import com.servicec.payment.service.PaymentService;
import com.servicec.payment.service.SagaClientService;
import com.servicec.payment.service.TaskExecutor;
import com.servicec.payment.service.TaskService;
import com.servicec.payment.type.TaskStatus;
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
    private final PaymentService paymentService;
    private final TaskService taskService;
    private final SagaClientService sagaClientService;
    private final Random rand = new Random();

    public TaskExecutorImpl(final PaymentService paymentService,
                            final TaskService taskService,
                            final SagaClientService sagaClientService) {
        this.paymentService = paymentService;
        this.taskService = taskService;
        this.sagaClientService = sagaClientService;
    }

    @Override
    public void execute(final Task task) {
        log.info("Looking room for task with id: {}", task.getId());
        ResultDto result;
        try {
            if (isCancelled(task.getId())) {
                return;
            }
            for (int i = 0; i < 100; i += 10) {
                log.info("Task processing..." + i + "%");
                Thread.sleep(1000L);

                if (rand.nextInt(100) + 1 < 5) {
                    // imitation of an exceptional situation
                    throw new IllegalArgumentException();
                }
            }

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setTaskId(task.getId());
            paymentService.create(paymentDto);

            result = taskService.finishTask(task.getId());
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
