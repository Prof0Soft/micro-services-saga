package com.servicec.servicec.service;

import com.servicec.servicec.dto.TaskInfoDto;
import com.servicec.servicec.dto.TaskStatusDto;

/**
 * @author M.Bezmen
 */
public interface TaskService {
    TaskInfoDto createTask();

    TaskStatusDto getTaskStatus(final String taskId);

    TaskStatusDto cancelTask(String taskId);

    TaskStatusDto revertTask(String taskId);
}
