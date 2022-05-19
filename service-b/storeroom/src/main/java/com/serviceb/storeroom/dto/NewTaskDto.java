package com.serviceb.storeroom.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NewTaskDto {

    private UUID taskId;
    private String serviceName;
}
