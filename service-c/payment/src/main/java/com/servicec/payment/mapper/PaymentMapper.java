package com.servicec.payment.mapper;

import com.servicec.payment.dto.PaymentDto;
import com.servicec.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentMapper {

    Payment toEntity(PaymentDto order);

    PaymentDto toDto(Payment payment);
}
