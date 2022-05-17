package com.servicea.servicea.service;

import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.dto.TaskStatusDto;

/**
 * @author M.Bezmen
 */
public interface TaskService {
    TaskInfoDto createTask();

    TaskStatusDto getTaskStatus(final String taskId);

    TaskStatusDto cancelTask(String taskId);

    TaskStatusDto revertTask(String taskId);
}
