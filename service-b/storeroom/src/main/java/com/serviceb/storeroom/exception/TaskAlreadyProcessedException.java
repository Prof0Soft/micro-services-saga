package com.serviceb.storeroom.exception;

import java.util.UUID;

public class TaskAlreadyProcessedException extends BadRequestException {

    public TaskAlreadyProcessedException(UUID taskId) {
        super("Task " + taskId + " already running");
    }
}
