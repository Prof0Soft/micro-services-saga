package com.servicessagaorchestrator.servicessagaorchestrator.service.impl;

import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Step;
import com.servicessagaorchestrator.servicessagaorchestrator.service.SagaService;
import com.servicessagaorchestrator.servicessagaorchestrator.service.client.StartProcessClientService;
import com.servicessagaorchestrator.servicessagaorchestrator.type.ServiceName;
import com.servicessagaorchestrator.servicessagaorchestrator.type.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Service
public class SagaOrchestratorServiceImpl implements SagaService {

    private final Map<ServiceName, StartProcessClientService> clients;

    public SagaOrchestratorServiceImpl(final Map<ServiceName, StartProcessClientService> clients) {
        this.clients = clients;
    }


    @Override
    public void startSaga(final SagaProcess flow) {
        final Optional<Step> nextStep = getNextStep(flow);

        nextStep.ifPresent(step -> {
            final StartProcessClientService startProcessClientService = clients.get(step.getServiceName());
         var result =   startProcessClientService.createTask();
         flow.setActiveStepId(step.getId());
        });
    }

    private Optional<Step> getNextStep(final SagaProcess flow) {
        if (flow.getStatus() == TaskStatus.NEW) {
            return flow.getSteps().stream()
                    .filter(step -> step.getFlowOrder() == 1)
                    .findFirst();
        }

        final Long activeStepId = flow.getActiveStepId();
        final Optional<Step> activeStep = flow.getSteps().stream()
                .filter(step -> step.getId().equals(activeStepId))
                .findFirst();

        return flow.getSteps().stream()
                .filter(step -> step.getFlowOrder() == activeStep.get().getFlowOrder() + 1)
                .findFirst();
    }
}
