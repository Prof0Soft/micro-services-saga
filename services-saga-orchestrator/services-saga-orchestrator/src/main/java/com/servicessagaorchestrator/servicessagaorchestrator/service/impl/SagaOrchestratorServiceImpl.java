package com.servicessagaorchestrator.servicessagaorchestrator.service.impl;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Step;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.OrderRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.SagaProcessRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.service.SagaService;
import com.servicessagaorchestrator.servicessagaorchestrator.service.client.StartProcessClientService;
import com.servicessagaorchestrator.servicessagaorchestrator.type.ServiceName;
import com.servicessagaorchestrator.servicessagaorchestrator.type.TaskStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Service
public class SagaOrchestratorServiceImpl implements SagaService {

    private final Map<ServiceName, StartProcessClientService> clients;
    private final OrderRepository orderRepository;
    private final SagaProcessRepository sagaProcessRepository;

    public SagaOrchestratorServiceImpl(final Map<ServiceName, StartProcessClientService> clients,
                                       final OrderRepository orderRepository, final SagaProcessRepository sagaProcessRepository) {
        this.clients = clients;
        this.orderRepository = orderRepository;
        this.sagaProcessRepository = sagaProcessRepository;
    }

    @Transactional
    @Override
    public void initSaga(final SagaProcess flow) {
        final Optional<Step> nextStepOptional = getNextStep(flow);
        try {
            submitNextStep(nextStepOptional, flow);
        } catch (Exception e) {
            final Order order = flow.getOrder();
            order.setStatus(TaskStatus.FAILED);
            orderRepository.save(order);
            throw e;
        }
    }

    @Override
    public void nextSagaStep(final ResultDto result) {
        if (result.getStatus().equals(TaskStatus.DONE)) {
            handleDoneStep(result);
        }
        if (result.getStatus().equals(TaskStatus.FAILED)) {
            handleFailedStep(result);
        }
    }

    private void handleFailedStep(final ResultDto result) {
        Order order = null;
        try {
            final String taskId = result.getTaskId();
            final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);
            order = flow.getOrder();
            flow.getSteps().stream()
                    .filter(step -> !Objects.equals(step.getId(), flow.getActiveStepId()))
                    .forEach(step -> {
                        final Optional<Step> previousStepOptional = getPreviousStep(flow);
                        submitRevert(previousStepOptional, taskId);
                    });
        } catch (Exception e) {
            if (order != null) {
                order.setStatus(TaskStatus.FAILED);
                orderRepository.save(order);
            }
            throw e;
        }
    }

    private void handleDoneStep(final ResultDto result) {
        final String taskId = result.getTaskId();
        final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);
        final Optional<Step> nextStepOptional = getNextStep(flow);
        submitNextStep(nextStepOptional, flow);
    }

    private Optional<Step> getPreviousStep(final SagaProcess flow) {
        final Integer STEP = 1;

        final Long activeStepId = flow.getActiveStepId();
        final Optional<Step> activeStep = flow.getSteps().stream().filter(step -> step.getId().equals(activeStepId)).findFirst();
        if (activeStep.isEmpty()) {
            throw new IllegalStateException("Active step not found");
        }
        return flow.getSteps().stream().filter(step -> step.getFlowOrder() == activeStep.get().getFlowOrder() - STEP).findFirst();
    }

    void submitRevert(final Optional<Step> previousStepOptional, final String taskId) {
        previousStepOptional.ifPresent(step -> clients.get(step.getServiceName()).revertTask(taskId));
    }

    void submitNextStep(final Optional<Step> nextStepOptional, final SagaProcess flow) {
        nextStepOptional.ifPresent(step -> {
            final StartProcessClientService startProcessClientService = clients.get(step.getServiceName());
            startProcessClientService.createTask(flow.getOrder().getId().toString());
            flow.setActiveStepId(step.getId());
        });
    }

    private Optional<Step> getNextStep(final SagaProcess flow) {
        final Integer FIRST_STEP = 1;
        if (flow.getStatus() == TaskStatus.NEW) {
            return flow.getSteps().stream()
                    .filter(step -> step.getFlowOrder().equals(FIRST_STEP))
                    .findFirst();
        }

        final Long activeStepId = flow.getActiveStepId();
        final Step activeStep = flow.getSteps().stream()
                .filter(step -> step.getId().equals(activeStepId))
                .findFirst().orElseThrow();

        return flow.getSteps().stream()
                .filter(step -> step.getFlowOrder() == activeStep.getFlowOrder() + 1)
                .findFirst();
    }
}
