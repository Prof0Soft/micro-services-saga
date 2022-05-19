package com.servicessagaorchestrator.servicessagaorchestrator.service;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;

import java.util.UUID;

/**
 * @author Sergey B.
 * 18.05.2022
 */
public interface SagaService {

    void initSaga(final SagaProcess flow);

    void nextSagaStep(final ResultDto result);

    void cancelSaga(final UUID taskId);

}
