package com.serviceb.serviceb.service;

import com.serviceb.serviceb.dto.TaskInfoDto;
import com.serviceb.serviceb.dto.TaskStatusDto;

/**
 * @author M.Bezmen
 */
public interface TaskService {
    TaskInfoDto createTask();

    TaskStatusDto getTaskStatus(final String taskId);

    TaskStatusDto cancelTask(String taskId);

    TaskStatusDto revertTask(String taskId);
}
