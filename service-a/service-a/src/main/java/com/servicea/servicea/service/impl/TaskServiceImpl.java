package com.servicea.servicea.service.impl;

import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.dto.TaskStatusDto;
import com.servicea.servicea.entity.Task;
import com.servicea.servicea.mapper.TaskMapper;
import com.servicea.servicea.repository.TaskRepository;
import com.servicea.servicea.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
