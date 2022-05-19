package com.serviceb.storeroom.exception;

import java.util.UUID;

public class TaskNotFinishedException extends BadRequestException {

    public TaskNotFinishedException(final UUID taskId) {
        super("Task with id " + taskId  + " doesn't finished");
    }
}
