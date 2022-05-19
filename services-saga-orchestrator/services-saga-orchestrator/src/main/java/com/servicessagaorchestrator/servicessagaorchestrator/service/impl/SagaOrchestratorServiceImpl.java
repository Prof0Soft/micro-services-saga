package com.servicessagaorchestrator.servicessagaorchestrator.service.impl;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Order;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Step;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.BookingFlow;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;
import com.servicessagaorchestrator.servicessagaorchestrator.exception.BadRequestException;
import com.servicessagaorchestrator.servicessagaorchestrator.exception.NotFoundException;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.OrderRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.repository.SagaProcessRepository;
import com.servicessagaorchestrator.servicessagaorchestrator.service.SagaService;
import com.servicessagaorchestrator.servicessagaorchestrator.service.client.StartProcessClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Sergey B.
 * 18.05.2022
 */
@Slf4j
@Service
public class SagaOrchestratorServiceImpl implements SagaService {
    private static final Integer FIRST_STEP = 1;
    private static final Integer STEP = 1;
    private final Map<BookingFlow, StartProcessClientService> clients;
    private final OrderRepository orderRepository;
    private final SagaProcessRepository sagaProcessRepository;

    public SagaOrchestratorServiceImpl(final Map<BookingFlow, StartProcessClientService> clients,
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
            final Order order = flow.getOrder();
            order.setStatus(TaskStatus.RUNNING);
            orderRepository.save(order);
        } catch (Exception ex) {
            final Order order = flow.getOrder();
            order.setStatus(TaskStatus.FAILED);
            orderRepository.save(order);
            throw ex;
        }
    }

    @Override
    public void nextSagaStep(final ResultDto result) {
        if (result.getStatus() == TaskStatus.REVERTED) {
            log.info("Service: {} -> Task with id {} has been reverted.", result.getServiceName(), result.getTaskId());
            return;
        }

        if (result.getStatus().equals(TaskStatus.FAILED)) {
            handleFailedStep(result);
            return;
        }

        final String taskId = result.getTaskId();
        final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);

        if (!isNotCanceled(flow)) {
            revertFlow(taskId);
            return;
        }

        if (result.getStatus().equals(TaskStatus.DONE)) {
            handleDoneStep(result);
        }
    }

    @Transactional
    @Override
    public void cancelSaga(final String taskId) {
        final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);
        if (flow == null) {
            throw new NotFoundException(taskId);
        }
        final Order order = flow.getOrder();
        if (order.getStatus() != TaskStatus.RUNNING) {
            throw new BadRequestException(taskId);
        }
        order.setStatus(TaskStatus.CANCELED);
        sagaProcessRepository.save(flow);
        Long activeStepId = flow.getActiveStepId();
        Optional<Step> activeStepOptional = flow.getSteps().stream()
                .filter(step -> step.getId() > activeStepId).findFirst();

        if (activeStepOptional.isPresent()) {
            try {
                clients.get(activeStepOptional.get().getBookingFlow()).cancelTask(taskId);
            } catch (Exception ex) {
                log.error("Error while canceling step {}", activeStepOptional.get(), ex);
            }
        }
        revertFlow(taskId);
    }

    private void handleFailedStep(final ResultDto result) {
        Order order = null;
        try {
            final String taskId = result.getTaskId();
            final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);
            order = flow.getOrder();
            revertFlow(taskId);
        } catch (Exception e) {
            if (order != null) {
                order.setStatus(TaskStatus.FAILED);
                orderRepository.save(order);
            }
            throw e;
        }
    }

    private void revertFlow(final String taskId) {
        final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);
        flow.getSteps().stream()
                .filter(step -> !Objects.equals(step.getId(), flow.getActiveStepId()))
                .forEach(step -> {
                    final Optional<Step> previousStepOptional = getPreviousStep(flow);
                    submitRevert(previousStepOptional, taskId);
                });
    }

    private void handleDoneStep(final ResultDto result) {
        final String taskId = result.getTaskId();
        final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);
        final Optional<Step> nextStepOptional = getNextStep(flow);
        if (isNotCanceled(flow)) {
            submitNextStep(nextStepOptional, flow);
        }
    }

    private boolean isNotCanceled(final SagaProcess flow) {
        return flow.getOrder().getStatus() != TaskStatus.CANCELED;
    }

    private Optional<Step> getPreviousStep(final SagaProcess flow) {
        final Long activeStepId = flow.getActiveStepId();
        final Step activeStep = flow.getSteps().stream()
                .filter(step -> step.getId().equals(activeStepId))
                .findFirst().orElseThrow(()-> new IllegalStateException("Active step not found"));
        return flow.getSteps().stream()
                .filter(step -> step.getFlowOrder() == activeStep.getFlowOrder() - STEP)
                .findFirst();
    }

    void submitRevert(final Optional<Step> previousStepOptional, final String taskId) {
        previousStepOptional.ifPresent(step -> clients.get(step.getBookingFlow()).revertTask(taskId));
    }

    void submitNextStep(final Optional<Step> nextStepOptional, final SagaProcess flow) {
        nextStepOptional.ifPresent(step -> {
            final StartProcessClientService startProcessClientService = clients.get(step.getBookingFlow());
            startProcessClientService.createTask(flow.getOrder().getId().toString());
            flow.setActiveStepId(step.getId());
        });
    }

    private Optional<Step> getNextStep(final SagaProcess flow) {
        if (flow.getStatus() == TaskStatus.NEW) {
            return flow.getSteps().stream()
                    .filter(step -> step.getFlowOrder().equals(FIRST_STEP))
                    .findFirst();
        }

        final Long activeStepId = flow.getActiveStepId();
        final Step activeStep = flow.getSteps().stream()
                .filter(step -> step.getId().equals(activeStepId))
                .findFirst().orElseThrow(()-> new IllegalStateException("Active step not found"));

        return flow.getSteps().stream()
                .filter(step -> step.getFlowOrder() == activeStep.getFlowOrder() + 1)
                .findFirst();
    }
}
