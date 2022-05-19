package com.servicea.order.service.impl;

import com.servicea.order.dto.ResultDto;
import com.servicea.order.service.SagaClientService;
import com.servicea.order.service.client.SagaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SagaClientServiceImpl implements SagaClientService {

    private final SagaClient sagaClient;

    public SagaClientServiceImpl(final SagaClient sagaClient) {
        this.sagaClient = sagaClient;
    }

    @Override
    public void reply(ResultDto result) {
        log.info("send result to orchestrator, task id {}", result.getTaskId());
        sagaClient.sendResult(result);
    }
}
