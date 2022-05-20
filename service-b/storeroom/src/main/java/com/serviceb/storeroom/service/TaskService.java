package com.serviceb.storeroom.service;

import com.serviceb.storeroom.dto.NewTaskDto;
import com.serviceb.storeroom.dto.ResultDto;
import com.serviceb.storeroom.dto.TaskInfoDto;
import com.serviceb.storeroom.dto.TaskStatusDto;
import com.serviceb.storeroom.type.TaskStatus;
import com.serviceb.storeroom.entity.Task;

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

    Task updateTaskStatusById(UUID id, TaskStatus canceled);
}
