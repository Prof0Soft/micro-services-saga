package com.servicec.servicec.service.impl;

import com.servicec.servicec.dto.TaskInfoDto;
import com.servicec.servicec.dto.TaskStatusDto;
import com.servicec.servicec.entity.Task;
import com.servicec.servicec.exception.BadRequestException;
import com.servicec.servicec.exception.NotFoundException;
import com.servicec.servicec.mapper.TaskMapper;
import com.servicec.servicec.repository.TaskRepository;
import com.servicec.servicec.service.TaskService;
import com.servicec.servicec.type.TaskStatus;
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

    @Transactional
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
