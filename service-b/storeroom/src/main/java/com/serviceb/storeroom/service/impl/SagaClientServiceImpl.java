package com.serviceb.storeroom.service.impl;

import com.serviceb.storeroom.dto.ResultDto;
import com.serviceb.storeroom.service.SagaClientService;
import com.serviceb.storeroom.service.client.SagaClient;
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
