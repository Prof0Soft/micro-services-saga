package com.servicessagaorchestrator.servicessagaorchestrator.mockdata;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.BookingFlow;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;

import java.util.UUID;

/**
 * @author Sergey B.
 * 19.05.2022
 */
public class ResultDtoMock {
    public static ResultDto create() {
        final ResultDto resultDto = new ResultDto();
        resultDto.setStatus(TaskStatus.CREATED);
        resultDto.setServiceName(BookingFlow.SERVICE_A.name());
        resultDto.setTaskId(UUID.randomUUID());
        return resultDto;
    }
}
