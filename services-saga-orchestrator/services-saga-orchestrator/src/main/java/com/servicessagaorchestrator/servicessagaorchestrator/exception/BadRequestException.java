package com.servicessagaorchestrator.servicessagaorchestrator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author M.Bezmen
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(final String taskId, final String message) {
        super("Task with id: " + taskId + message);
    }
}
