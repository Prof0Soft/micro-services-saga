package com.servicec.payment.service.impl;

import com.servicec.payment.dto.NewTaskDto;
import com.servicec.payment.dto.ResultDto;
import com.servicec.payment.dto.TaskInfoDto;
import com.servicec.payment.dto.TaskStatusDto;
import com.servicec.payment.entity.Task;
import com.servicec.payment.exception.TaskNotFinishedException;
import com.servicec.payment.exception.TaskNotFoundException;
import com.servicec.payment.exception.TaskNotRunningException;
import com.servicec.payment.mapper.TaskMapper;
import com.servicec.payment.repository.TaskRepository;
import com.servicec.payment.service.PaymentService;
import com.servicec.payment.service.TaskService;
import com.servicec.payment.type.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final PaymentService paymentService;

    public TaskServiceImpl(final TaskRepository repository,
                           final TaskMapper mapper,
                           final PaymentService paymentService) {
        this.repository = repository;
        this.mapper = mapper;
        this.paymentService = paymentService;
    }

    @Transactional
    @Override
    public TaskInfoDto createTask(final NewTaskDto newTaskDto) {
        final Task task = new Task();
        task.setId(newTaskDto.getTaskId());
        task.setServiceName(newTaskDto.getServiceName());

        final Task savedTask = repository.save(task);

        log.info("Task {} created", savedTask.getId());
        log.debug("Task {} created", savedTask);

        return mapper.toInfoDto(savedTask);
    }

    @Transactional(readOnly = true)
    @Override
    public TaskStatusDto getTaskStatus(final UUID taskId) {
        return repository.findById(taskId)
                .map(mapper::toStatusDto)
                .orElseThrow(() -> {
                    log.error("Task with id: {} not found", taskId);
                    throw new TaskNotFoundException(taskId);
                });
    }

    @Transactional
    @Override
    public TaskStatusDto cancelTask(final UUID taskId) {
        Task task = repository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task with id: {} not found.", taskId);
                    return new TaskNotFoundException(taskId);
                });
        if (!TaskStatus.RUNNING.equals(task.getStatus())) {
            log.error("Task with id: {} not running. Task: {}", task.getId(), task);
            throw new TaskNotRunningException(taskId);
        }
        task = updateTaskStatusById(taskId, TaskStatus.CANCELING);
        return mapper.toStatusDto(task);
    }

    @Transactional
    @Override
    public TaskStatusDto revertTask(final UUID taskId) {
        try {
            Task task = repository.findById(taskId)
                    .orElseThrow(() -> {
                        log.error("Task with id: {} not found.", taskId);
                        return new TaskNotFoundException(taskId);
                    });
            if (!TaskStatus.DONE.equals(task.getStatus())) {
                log.error("Task with id: {} not done. Task: {}", task.getId(), task);
                throw new TaskNotFinishedException(taskId);
            }
            paymentService.removeByTaskId(task.getId());

            task = updateTaskStatusById(taskId, TaskStatus.REVERTED);
            log.info("Task {} reverted", task.getId());
            log.debug("Task {} reverted", task);
            return mapper.toStatusDto(task);
        } catch (Exception e) {
            log.error("Error reverting task {}", taskId, e);
            Task task = updateTaskStatusById(taskId, TaskStatus.FAILED);
            return mapper.toStatusDto(task);
        }
    }

    @Transactional
    @Override
    public ResultDto finishTask(final UUID taskId) {
        Task task = updateTaskStatusById(taskId, TaskStatus.DONE);
        if (task.getStatus() != TaskStatus.RUNNING) {
            throw new TaskNotRunningException(taskId);
        }
        return mapper.toResultDto(task);
    }

    @Transactional
    @Override
    public ResultDto failTask(final UUID taskId) {
        Task task = updateTaskStatusById(taskId, TaskStatus.FAILED);
        return mapper.toResultDto(task);
    }

    private Task updateTaskStatusById(final UUID taskId, final TaskStatus newStatus) {
        return repository.findById(taskId).map(task -> {
            task.setStatus(newStatus);
            return repository.save(task);
        }).orElseThrow(() -> new IllegalArgumentException("Can't update task status for task: " + taskId));

    }
}
