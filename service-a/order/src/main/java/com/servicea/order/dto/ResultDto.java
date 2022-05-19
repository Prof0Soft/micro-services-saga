package com.servicea.order.dto;

import com.servicea.order.type.TaskStatus;
import lombok.Data;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Data
public class ResultDto {
    private UUID taskId;
    private String serviceName;
    private TaskStatus status;
}
