package com.servicessagaorchestrator.servicessagaorchestrator.enums;

import com.servicessagaorchestrator.servicessagaorchestrator.entity.Step;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Sergey B.
 * 18.05.2022
 */
public enum BookingFlow {
    SERVICE_A,
    SERVICE_B,
    SERVICE_C;

    public static List<Step> buildFlow() {
        AtomicInteger order = new AtomicInteger(1);
        return Arrays.stream(BookingFlow.values())
                .map(bookingFlow -> {
                    final Step step = new Step();
                    step.setFlowOrder(order.get());
                    step.setStatus(TaskStatus.NEW);
                    step.setBookingFlow(bookingFlow);
                    order.getAndIncrement();

                    return step;
                })
                .collect(Collectors.toList());
    }
}
