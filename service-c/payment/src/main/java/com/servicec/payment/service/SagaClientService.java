package com.servicec.payment.service;

import com.servicec.payment.dto.ResultDto;

public interface SagaClientService {

    void reply(ResultDto result);
}
