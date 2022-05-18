package com.servicec.servicec.service.client;

import com.servicec.servicec.config.SagaClientConfig;
import com.servicec.servicec.dto.ResultDto;
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
