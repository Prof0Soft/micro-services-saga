package com.serviceb.serviceb.service.impl;

import com.serviceb.serviceb.dto.TaskInfoDto;
import com.serviceb.serviceb.dto.TaskStatusDto;
import com.serviceb.serviceb.entity.Task;
import com.serviceb.serviceb.mapper.TaskMapper;
import com.serviceb.serviceb.repository.TaskRepository;
import com.serviceb.serviceb.service.TaskService;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public TaskStatusDto cancelTask(final String taskId) {
        return null;
    }

    @Override
    public TaskStatusDto revertTask(final String taskId) {
        return null;
    }
}
