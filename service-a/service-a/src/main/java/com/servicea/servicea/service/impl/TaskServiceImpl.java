package com.servicea.servicea.service.impl;

import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.dto.TaskStatusDto;
import com.servicea.servicea.entity.Task;
import com.servicea.servicea.exception.BadRequestException;
import com.servicea.servicea.exception.NotFoundException;
import com.servicea.servicea.mapper.TaskMapper;
import com.servicea.servicea.repository.TaskRepository;
import com.servicea.servicea.service.Reverter;
import com.servicea.servicea.service.TaskService;
import com.servicea.servicea.type.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final Reverter revert;

    public TaskServiceImpl(final TaskRepository repository, final TaskMapper mapper, final Reverter revert) {
        this.repository = repository;
        this.mapper = mapper;
        this.revert = revert;
    }

    @Transactional
    @Override
    public TaskInfoDto createTask(final String taskId) {
        final Task task = new Task();
        task.setId(UUID.fromString(taskId));

        final Task savedTask = repository.save(task);

        log.info("Task {} created", savedTask.getId());
        log.debug("Task {} created", savedTask);

        return mapper.toDto(savedTask);
    }

    @Override
    public TaskStatusDto getTaskStatus(final String taskId) {
        return repository.findById(UUID.fromString(taskId))
                .map(mapper::toStatusDto)
                .orElseThrow(() -> {
                    log.error("Task with id: {} not found", taskId);
                    throw new NotFoundException(taskId);
                });
    }

    @Transactional
    @Override
    public TaskStatusDto cancelTask(final String taskId) {
        return repository.findById(UUID.fromString(taskId))
                .map(task -> {
                    if (!task.getStatus().equals(TaskStatus.RUNNING)) {
                        log.error("Task with id: {} not running. Task: {}", task.getId(), task);
                        throw new BadRequestException(taskId, "is not running.");
                    }
                    task.setStatus(TaskStatus.CANCELING);
                    return mapper.toStatusDto(task);
                })
                .orElseThrow(() -> {
                    log.error("Task with id: {} not found.", taskId);
                    throw new NotFoundException(taskId);
                });
    }

    @Transactional
    @Override
    public TaskStatusDto revertTask(final String taskId) {
        try {
            Optional<Task> taskOptional = repository.findById(UUID.fromString(taskId));
            if (taskOptional.isEmpty()) {
                log.error("Task with id: {} not found.", taskId);
                throw new NotFoundException(taskId);
            }
            final Task task = taskOptional.get();
            if (!task.getStatus().equals(TaskStatus.DONE)) {
                log.error("Task with id: {} not done. Task: {}", task.getId(), task);
                throw new BadRequestException(taskId, "is not done.");
            }
            revert.revert(task);
            return mapper.toStatusDto(task);
        } catch (NotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error reverting task {}", taskId, e);
            return mapper.toStatusDto(updateTaskStatus(taskId, TaskStatus.FAILED));
        }
    }

    private Task updateTaskStatus(final String taskId, final TaskStatus newStatus) {
        return repository.findById(UUID.fromString(taskId)).map(task -> {
            task.setStatus(newStatus);
            return repository.save(task);
        }).orElseThrow(() -> new IllegalArgumentException("Can't update task status for task: " + taskId));

    }
}
