package com.servicec.servicec.service.impl;

import com.servicec.servicec.dto.TaskInfoDto;
import com.servicec.servicec.dto.TaskStatusDto;
import com.servicec.servicec.service.TaskService;
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
