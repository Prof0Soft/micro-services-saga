package com.servicec.payment.service;

import com.servicec.payment.dto.PaymentDto;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
public interface PaymentService {

    PaymentDto create(PaymentDto order);

    void removeByTaskId(UUID taskId);
}
