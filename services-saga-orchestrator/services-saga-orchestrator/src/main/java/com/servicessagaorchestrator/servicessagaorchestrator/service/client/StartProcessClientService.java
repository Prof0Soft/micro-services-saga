package com.servicessagaorchestrator.servicessagaorchestrator.service.client;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskInfoDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskStatusDto;

/**
 * @author Sergey B.
 * 18.05.2022
 */
public interface StartProcessClientService {
    TaskInfoDto createTask(String taskId);

    TaskStatusDto cancelTask(String taskId);

    TaskStatusDto revertTask(String taskId);
}
