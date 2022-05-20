package com.servicec.payment.service.impl;

import com.servicec.payment.dto.PaymentDto;
import com.servicec.payment.entity.Payment;
import com.servicec.payment.entity.Task;
import com.servicec.payment.mapper.PaymentMapper;
import com.servicec.payment.repository.PaymentRepository;
import com.servicec.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Slf4j
@Component
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(final PaymentRepository paymentRepository,
                              final PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Transactional
    @Override
    public PaymentDto create(PaymentDto paymentDto) {

        Task task = new Task();
        task.setId(paymentDto.getTaskId());

        Payment payment = new Payment();
        payment.setTask(task);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    @Transactional
    @Override
    public void removeByTaskId(final UUID taskId) {
        paymentRepository.removeByTaskId(taskId);
    }
}
