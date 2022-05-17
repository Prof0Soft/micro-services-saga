package com.servicec.servicec.service.impl;

import com.servicec.servicec.dto.TaskInfoDto;
import com.servicec.servicec.dto.TaskStatusDto;
import com.servicec.servicec.entity.Task;
import com.servicec.servicec.mapper.TaskMapper;
import com.servicec.servicec.repository.TaskRepository;
import com.servicec.servicec.service.TaskService;
import org.springframework.stereotype.Service;

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
