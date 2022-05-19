package com.serviceb.storeroom.service.impl;

import com.serviceb.storeroom.dto.NewTaskDto;
import com.serviceb.storeroom.dto.ResultDto;
import com.serviceb.storeroom.dto.TaskInfoDto;
import com.serviceb.storeroom.dto.TaskStatusDto;
import com.serviceb.storeroom.entity.Task;
import com.serviceb.storeroom.exception.TaskAlreadyProcessedException;
import com.serviceb.storeroom.exception.TaskNotFinishedException;
import com.serviceb.storeroom.exception.TaskNotFoundException;
import com.serviceb.storeroom.exception.TaskNotRunningException;
import com.serviceb.storeroom.mapper.TaskMapper;
import com.serviceb.storeroom.repository.TaskRepository;
import com.serviceb.storeroom.service.ItemReservationService;
import com.serviceb.storeroom.service.TaskService;
import com.serviceb.storeroom.type.TaskStatus;
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
    private final ItemReservationService itemReservationService;

    public TaskServiceImpl(final TaskRepository repository,
                           final TaskMapper mapper,
                           final ItemReservationService itemReservationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.itemReservationService = itemReservationService;
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
            itemReservationService.removeByTaskId(task.getId());

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
    public ResultDto runTask(final UUID taskId) {
        TaskStatusDto taskStatus = getTaskStatus(taskId);
        if (TaskStatus.CREATED != taskStatus.getStatus()) {
            throw new TaskAlreadyProcessedException(taskId);
        }

        Task task = updateTaskStatusById(taskId, TaskStatus.RUNNING);
        return mapper.toResultDto(task);
    }

    @Transactional
    @Override
    public ResultDto finishTask(final UUID taskId) {
        TaskStatusDto taskStatus = getTaskStatus(taskId);
        if (TaskStatus.RUNNING != taskStatus.getStatus()) {
            throw new TaskNotRunningException(taskId);
        }

        Task task = updateTaskStatusById(taskId, TaskStatus.DONE);
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
