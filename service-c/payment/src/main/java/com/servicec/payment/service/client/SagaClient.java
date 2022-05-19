package com.servicec.payment.service.client;

import com.servicec.payment.config.SagaClientConfig;
import com.servicec.payment.dto.ResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author M.Bezmen
 */
@FeignClient(name = "saga-client", configuration = SagaClientConfig.class, url = "${client.saga.host}")
public interface SagaClient {

    @PostMapping("${client.saga.result}")
    ResponseEntity<?> sendResult(@RequestBody ResultDto result);
}
