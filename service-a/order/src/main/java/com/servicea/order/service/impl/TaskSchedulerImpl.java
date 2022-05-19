package com.servicea.order.service.impl;

import com.servicea.order.repository.TaskRepository;
import com.servicea.order.service.TaskExecutor;
import com.servicea.order.service.TaskScheduler;
import com.servicea.order.type.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author M.Bezmen
 */
@Slf4j
@Component
public class TaskSchedulerImpl implements TaskScheduler {

    private final ThreadPoolTaskExecutor schedulerExecutor;
    private final TaskRepository repository;
    private final TaskExecutor taskExecutor;

    public TaskSchedulerImpl(final TaskRepository repository,
                             final TaskExecutor taskExecutor) {
        this.repository = repository;
        this.taskExecutor = taskExecutor;
        schedulerExecutor = new ThreadPoolTaskExecutor();
        schedulerExecutor.setThreadNamePrefix("scheduler-pool");
        schedulerExecutor.setMaxPoolSize(4);
        schedulerExecutor.setCorePoolSize(4);
        schedulerExecutor.initialize();

    }

    @Scheduled(cron = "${scheduler-task.cron-expression:*/1 * * * * *}", zone = "UTC")
    @Override
    public void getTaskExecution() {
        repository.findByStatus(TaskStatus.CREATED).forEach(task -> {
            log.info("Task {} is scheduled for execution", task.getId());
            log.debug("Task {} is scheduled for execution", task);
            schedulerExecutor.execute(() -> {
                taskExecutor.execute(task);
            });
        });
    }

    @PostConstruct
    private void errorFixer() {
        repository.findByStatus(TaskStatus.RUNNING).forEach(task -> {
            task.setStatus(TaskStatus.CREATED);
            repository.save(task);
        });
    }
}
