package com.servicea.servicea.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NewTaskDto {

    private UUID taskId;
    private String serviceName;
}
