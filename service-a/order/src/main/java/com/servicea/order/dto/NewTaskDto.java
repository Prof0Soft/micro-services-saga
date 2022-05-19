package com.servicea.order.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NewTaskDto {

    private UUID taskId;
    private String serviceName;
}
