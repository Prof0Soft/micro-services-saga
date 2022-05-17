package com.serviceb.serviceb.service.impl;

import com.serviceb.serviceb.dto.TaskInfoDto;
import com.serviceb.serviceb.dto.TaskStatusDto;
import com.serviceb.serviceb.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Override
    public TaskInfoDto createTask() {
        final TaskInfoDto taskInfoDto = new TaskInfoDto();
        taskInfoDto.setId(UUID.randomUUID().toString());
        return taskInfoDto;
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
