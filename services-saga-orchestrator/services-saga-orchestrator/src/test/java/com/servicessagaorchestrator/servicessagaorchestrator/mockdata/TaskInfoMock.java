package com.servicessagaorchestrator.servicessagaorchestrator.mockdata;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskInfoDto;

import java.util.UUID;

/**
 * @author Sergey B.
 * 19.05.2022
 */
public class TaskInfoMock {
    public static TaskInfoDto create() {
        final TaskInfoDto taskInfoDto = new TaskInfoDto();
        taskInfoDto.setId(UUID.randomUUID().toString());
        return taskInfoDto;
    }
}
