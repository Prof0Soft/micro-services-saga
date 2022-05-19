package com.servicec.payment.service;

import com.servicec.payment.dto.NewTaskDto;
import com.servicec.payment.dto.ResultDto;
import com.servicec.payment.dto.TaskInfoDto;
import com.servicec.payment.dto.TaskStatusDto;

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
