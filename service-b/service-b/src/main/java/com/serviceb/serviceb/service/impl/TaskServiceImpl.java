package com.serviceb.serviceb.service.impl;

import com.serviceb.serviceb.dto.TaskInfoDto;
import com.serviceb.serviceb.dto.TaskStatusDto;
import com.serviceb.serviceb.entity.Task;
import com.serviceb.serviceb.exception.BadRequestException;
import com.serviceb.serviceb.exception.NotFoundException;
import com.serviceb.serviceb.mapper.TaskMapper;
import com.serviceb.serviceb.repository.TaskRepository;
import com.serviceb.serviceb.service.TaskService;
import com.serviceb.serviceb.type.TaskStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskServiceImpl(final TaskRepository repository, final TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TaskInfoDto createTask() {
        final Task savedTask = repository.save(new Task());
        return mapper.toDto(savedTask);
    }

    @Override
    public TaskStatusDto getTaskStatus(final String taskId) {
        return repository.findById(UUID.fromString(taskId))
                .map(mapper::toStatusDto)
                .orElseThrow(() -> new NotFoundException(taskId));
    }

    @Transactional
    @Override
    public TaskStatusDto cancelTask(final String taskId) {
        return repository.findById(UUID.fromString(taskId))
                .map(task -> {
                    if (!task.getStatus().equals(TaskStatus.RUNNING)){
                        throw new BadRequestException(taskId);
                    }
                    task.setStatus(TaskStatus.CANCELING);
                    return mapper.toStatusDto(task);
                })
                .orElseThrow(() -> new NotFoundException(taskId));
    }

    @Override
    public TaskStatusDto revertTask(final String taskId) {
        return null;
    }
}
