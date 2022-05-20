package com.servicessagaorchestrator.servicessagaorchestrator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author M.Bezmen
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(final String taskId) {
        super("Order with id: " + taskId + "can't be canceling.");
    }
    public BadRequestException() {
        super();
    }
}
