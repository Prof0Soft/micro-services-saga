package com.servicea.servicea.service;

import com.servicea.servicea.dto.NewTaskDto;
import com.servicea.servicea.dto.ResultDto;
import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.dto.TaskStatusDto;
import com.servicea.servicea.type.TaskStatus;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
public interface TaskService {
    TaskInfoDto createTask(NewTaskDto task);

    TaskStatusDto getTaskStatus(UUID taskId);

    TaskStatusDto cancelTask(UUID taskId);

    TaskStatusDto revertTask(UUID taskId);

    ResultDto finishTask(final UUID taskId);

    ResultDto failTask(final UUID taskId);
}
