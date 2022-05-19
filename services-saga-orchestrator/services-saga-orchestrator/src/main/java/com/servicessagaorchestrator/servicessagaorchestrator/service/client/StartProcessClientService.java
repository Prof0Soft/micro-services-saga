package com.servicessagaorchestrator.servicessagaorchestrator.service.client;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskInfoDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskStatusDto;

import java.util.UUID;

/**
 * @author Sergey B.
 * 18.05.2022
 */
public interface StartProcessClientService {
    TaskInfoDto createTask(ResultDto taskId);

    TaskStatusDto cancelTask(UUID taskId);

    TaskStatusDto revertTask(UUID taskId);
}
