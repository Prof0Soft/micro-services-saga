package com.servicessagaorchestrator.servicessagaorchestrator.mockdata;

import com.servicessagaorchestrator.servicessagaorchestrator.entity.SagaProcess;
import com.servicessagaorchestrator.servicessagaorchestrator.entity.Step;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.BookingFlow;
import com.servicessagaorchestrator.servicessagaorchestrator.enums.TaskStatus;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Sergey B.
 * 19.05.2022
 */
public class SagaProcessMock {
    public static SagaProcess create() {
        final SagaProcess sagaProcess = new SagaProcess();
        sagaProcess.setActiveStepId(null);
        sagaProcess.setOrder(OrderMock.createOrder());
        sagaProcess.setSteps(BookingFlow.buildFlow());
        return sagaProcess;
    }

    public static SagaProcess create(final TaskStatus status) {

        AtomicLong count = new AtomicLong(1);

        final List<Step> steps = BookingFlow.buildFlow().stream()
                .peek(step -> step.setId(count.getAndIncrement()))
                .collect(Collectors.toList());

        final SagaProcess sagaProcess = new SagaProcess();
        sagaProcess.setActiveStepId(1L);
        sagaProcess.setOrder(OrderMock.createOrder(status));
        sagaProcess.setSteps(steps);

        return sagaProcess;
    }

    public static SagaProcess createLastStepFlow() {

        AtomicLong count = new AtomicLong(1);

        final List<Step> steps = BookingFlow.buildFlow().stream()
                .peek(step -> step.setId(count.getAndIncrement()))
                .collect(Collectors.toList());

        final SagaProcess sagaProcess = new SagaProcess();
        sagaProcess.setActiveStepId((long) steps.size());
        sagaProcess.setOrder(OrderMock.createOrder(TaskStatus.RUNNING));
        sagaProcess.setSteps(steps);

        return sagaProcess;
    }
}
