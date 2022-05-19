package com.servicea.order.service;

import com.servicea.order.dto.NewTaskDto;
import com.servicea.order.dto.ResultDto;
import com.servicea.order.dto.TaskInfoDto;
import com.servicea.order.dto.TaskStatusDto;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
public interface TaskService {
    TaskInfoDto createTask(NewTaskDto task);

    TaskStatusDto getTaskStatus(UUID taskId);

    TaskStatusDto cancelTask(UUID taskId);

    TaskStatusDto revertTask(UUID taskId);

    ResultDto runTask(final UUID taskId);

    ResultDto finishTask(final UUID taskId);

    ResultDto failTask(final UUID taskId);
}
