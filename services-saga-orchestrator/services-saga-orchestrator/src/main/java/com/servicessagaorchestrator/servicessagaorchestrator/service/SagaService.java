package com.servicessagaorchestrator.servicessagaorchestrator.service;

import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;

/**
 * @author Sergey B.
 * 18.05.2022
 */
public interface SagaService {

    void startSaga(final SagaProcess flow);
}
