package com.servicessagaorchestrator.servicessagaorchestrator.webhook;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import com.servicessagaorchestrator.servicessagaorchestrator.service.SagaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author M.Bezmen
 */
@RestController
public class ResultWebHook {
    private final SagaService sagaService;

    public ResultWebHook(final SagaService sagaService) {
        this.sagaService = sagaService;
    }

    @PostMapping("/result")
    public ResponseEntity<?> submitTaskResult(@RequestBody ResultDto result) {
        sagaService.nextSagaStep(result);
        return ResponseEntity.ok().build();
    }
}
