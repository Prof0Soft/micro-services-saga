package com.servicea.servicea.dto;

import com.servicea.servicea.type.TaskStatus;
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
