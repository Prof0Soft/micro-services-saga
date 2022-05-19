package com.servicea.servicea.exception;

import java.util.UUID;

public class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(final UUID taskId) {
        super("Task with id: " + taskId + " not found.");
    }
}
