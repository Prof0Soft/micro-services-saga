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
import java.util.UUID;

/**
 * The main service for processing services. More information about process
 *
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

    /**
     * Init saga process.
     *
     * @param flow {@link SagaProcess}
     * @throws Exception if create order on failed.
     */
    @Transactional
    @Override
    public void initSaga(final SagaProcess flow) {
        log.info("Initiate saga service.");
        try {
            final Optional<Step> nextStepOptional = getNextStep(flow);
            submitNextStep(nextStepOptional, flow);

            final Order order = flow.getOrder();
            order.setStatus(TaskStatus.RUNNING);

            sagaProcessRepository.save(flow);
            orderRepository.save(order);
        } catch (Exception ex) {
            log.error("Something went wrong. The process status will be FAILED.");
            final Order order = flow.getOrder();
            order.setStatus(TaskStatus.FAILED);

            orderRepository.save(order);
            throw ex;
        }
    }

    /**
     * Init next step of saga flow.
     *
     * @param result {@link ResultDto} dto with info about executed step.
     */
    @Transactional
    @Override
    public void nextSagaStep(final ResultDto result) {
        if (result.getStatus() == TaskStatus.REVERTED) {
            log.info("Service: {} -> Task with id {} has been reverted.", result.getServiceName(), result.getTaskId());
            return;
        }

        if (result.getStatus() == TaskStatus.FAILED) {
            log.info("Status");
            handleFailedStep(result);
            return;
        }

        final UUID taskId = result.getTaskId();
        final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);

        if (!isNotCanceled(flow)) {
            revertFlow(flow);
            return;
        }

        if (result.getStatus() == TaskStatus.DONE) {
            handleDoneStep(result);
        }

        sagaProcessRepository.save(flow);
    }

    /**
     * Cancel saga flow.
     *
     * @param taskId {@link UUID}
     */
    @Transactional
    @Override
    public void cancelSaga(final UUID taskId) {
        log.info("Run cancel process with task id {}", taskId);

        final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);
        canCancel(flow, taskId);

        final Order order = flow.getOrder();
        order.setStatus(TaskStatus.CANCELING);

        sagaProcessRepository.save(flow);

        final Optional<Step> activeStepOptional = getActiveStep(flow);

        try {
            if (activeStepOptional.isPresent()) {
                cancelCurrentStep(activeStepOptional.get(), taskId);
                final Optional<Step> previousStepOptional = getPreviousStep(flow);

                if (previousStepOptional.isEmpty()) {
                    order.setStatus(TaskStatus.CANCELED);
                    sagaProcessRepository.save(flow);
                    return;
                }
            }
            revertFlow(flow);
            order.setStatus(TaskStatus.CANCELED);
        } catch (Exception ex) {
            log.error("Error while canceling saga {}", taskId, ex);
            order.setStatus(TaskStatus.FAILED);
        } finally {
            sagaProcessRepository.save(flow);
        }
    }

    private Optional<Step> getActiveStep(final SagaProcess flow) {
        final Long activeStepId = flow.getActiveStepId();
        return flow.getSteps().stream()
                .filter(step -> Objects.equals(step.getId(), activeStepId))
                .findFirst();
    }

    private void canCancel(final SagaProcess flow, final UUID taskId) {
        if (flow == null) {
            log.warn("Flow object is null for task id {}", taskId);
            throw new NotFoundException(taskId.toString());
        }

        final Order order = flow.getOrder();
        if (order.getStatus() != TaskStatus.RUNNING) {
            log.warn("Can't run cancel process. The process isn't running.");
            throw new BadRequestException(taskId.toString());
        }
    }

    private void cancelCurrentStep(final Step currentStep, final UUID taskId) {
        try {
            clients.get(currentStep.getBookingFlow()).cancelTask(taskId);
        } catch (Exception ex) {
            log.error("Error while canceling step {}", currentStep, ex);
        }
    }

    private void handleFailedStep(final ResultDto result) {
        log.info("Handle failed step.");
        Order order = null;
        try {
            final UUID taskId = result.getTaskId();
            final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);
            order = flow.getOrder();
            revertFlow(flow);
            order.setStatus(TaskStatus.FAILED);
        } catch (Exception e) {
            if (order != null) {
                log.error("Can't handle filed step. The order is null.");
                order.setStatus(TaskStatus.FAILED);
                orderRepository.save(order);
            }
            throw e;
        }
    }

    private void revertFlow(final SagaProcess flow) {
        log.info("Run revert flow.");
        log.debug("Init revert for flow.");
        log.debug("With method objects: {}", flow);
        final Optional<Step> previousStepOptional = getPreviousStep(flow);

        if (previousStepOptional.isPresent()) {
            flow.setActiveStepId(previousStepOptional.get().getId());
            revertFlow(flow);
        }
        submitRevert(previousStepOptional, flow.getOrder().getId());
    }

    private void handleDoneStep(final ResultDto result) {
        log.debug("Handle done step.");
        log.debug("With method objects: {}", result);
        final UUID taskId = result.getTaskId();
        final SagaProcess flow = sagaProcessRepository.findByOrderId(taskId);
        final Optional<Step> nextStepOptional = getNextStep(flow);

        if (isNotCanceled(flow)) {
            submitNextStep(nextStepOptional, flow);
        }
    }

    private boolean isNotCanceled(final SagaProcess flow) {
        log.debug("Is not canceled.");
        log.debug("With method objects: {}", flow);
        return flow.getOrder().getStatus() != TaskStatus.CANCELED;
    }

    private Optional<Step> getPreviousStep(final SagaProcess flow) {
        log.debug("Get privius step.");
        log.debug("Method objects: {}", flow);
        final Step activeStep = getActiveStep(flow)
                .orElseThrow(() -> new IllegalStateException("Active step not found"));

        return flow.getSteps().stream()
                .filter(step -> step.getFlowOrder() == activeStep.getFlowOrder() - STEP)
                .findFirst();
    }

    void submitRevert(final Optional<Step> previousStepOptional, final UUID taskId) {
        log.debug("Submit revert.");
        log.debug("With method objects: {} {}", previousStepOptional, taskId);
        previousStepOptional.ifPresent(
                step -> clients.get(step.getBookingFlow()).revertTask(taskId));
    }

    void submitNextStep(final Optional<Step> nextStepOptional, final SagaProcess flow) {
        log.debug("Submit next step.");
        log.debug("Method objects: {} {}", nextStepOptional, flow);
        if (nextStepOptional.isPresent()) {
            final StartProcessClientService startProcessClientService = clients.get(nextStepOptional.get().getBookingFlow());

            final ResultDto resultDto = new ResultDto();
            resultDto.setTaskId(flow.getOrder().getId());
            resultDto.setServiceName("serviceNameStub");

            startProcessClientService.createTask(resultDto);
            flow.setActiveStepId(nextStepOptional.get().getId());
        } else {
            final Order order = flow.getOrder();
            order.setStatus(TaskStatus.DONE);
            orderRepository.save(order);
            sagaProcessRepository.save(flow);
        }
    }

    private Optional<Step> getNextStep(final SagaProcess flow) {
        log.debug("Get next step for task {}, active step id: {}", flow.getOrder().getId(), flow.getActiveStepId());
        if (flow.getOrder().getStatus() == TaskStatus.NEW) {
            return flow.getSteps().stream()
                    .filter(step -> step.getFlowOrder().equals(FIRST_STEP))
                    .findFirst();
        }

        final Step activeStep = getActiveStep(flow)
                .orElseThrow(() -> new IllegalStateException("Active step not found"));

        return flow.getSteps().stream()
                .filter(step -> step.getFlowOrder() == activeStep.getFlowOrder() + STEP)
                .findFirst();
    }
}
