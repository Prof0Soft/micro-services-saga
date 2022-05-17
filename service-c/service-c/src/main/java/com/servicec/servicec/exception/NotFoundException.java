package com.servicec.servicec.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author M.Bezmen
 */
@ResponseStatus(value = NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String taskId) {
        super("Task with id: " + taskId + " not found.");
    }
}
