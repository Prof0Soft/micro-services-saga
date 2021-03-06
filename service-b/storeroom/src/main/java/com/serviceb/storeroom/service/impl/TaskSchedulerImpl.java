package com.serviceb.storeroom.service.impl;

import com.serviceb.storeroom.repository.TaskRepository;
import com.serviceb.storeroom.service.TaskExecutor;
import com.serviceb.storeroom.service.TaskScheduler;
import com.serviceb.storeroom.service.TaskService;
import com.serviceb.storeroom.type.TaskStatus;
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
    private final TaskService taskService;

    public TaskSchedulerImpl(final TaskRepository repository,
                             final TaskExecutor taskExecutor,
                             final TaskService taskService) {
        this.repository = repository;
        this.taskExecutor = taskExecutor;
        this.taskService = taskService;
        schedulerExecutor = new ThreadPoolTaskExecutor();
        schedulerExecutor.setThreadNamePrefix("scheduler-pool");
        schedulerExecutor.setMaxPoolSize(4);
        schedulerExecutor.setCorePoolSize(4);
        schedulerExecutor.initialize();

    }

    @Scheduled(cron = "${scheduler-task.cron-expression:*/1 * * * * *}", zone = "UTC")
    @Override
    public void getTaskExecution() {
        if (schedulerExecutor.getActiveCount() != 0) {
            return;
        }

        repository.findByStatus(TaskStatus.CREATED).forEach(task -> {
            log.info("Task {} is scheduled for execution", task.getId());
            log.debug("Task {} is scheduled for execution", task);
            taskService.runTask(task.getId());
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
