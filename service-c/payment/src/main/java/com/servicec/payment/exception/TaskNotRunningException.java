package com.servicec.payment.exception;

import java.util.UUID;

public class TaskNotRunningException extends BadRequestException {

    public TaskNotRunningException(UUID taskId) {
        super("Task with id " + taskId  + " doesn't running");
    }
}
