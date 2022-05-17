package com.servicea.servicea.service.impl;

import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.dto.TaskStatusDto;
import com.servicea.servicea.service.TaskService;
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
