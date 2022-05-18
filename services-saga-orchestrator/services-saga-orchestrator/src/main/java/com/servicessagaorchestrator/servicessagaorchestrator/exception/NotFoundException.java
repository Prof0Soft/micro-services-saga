package com.servicessagaorchestrator.servicessagaorchestrator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author M.Bezmen
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundException extends RuntimeException {

    public NotFoundException(final String taskId) {
        super("Order with id: " + taskId + " not found.");
    }
}
