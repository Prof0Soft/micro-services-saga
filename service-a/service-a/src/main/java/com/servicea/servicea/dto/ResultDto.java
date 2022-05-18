package com.servicea.servicea.dto;

import com.servicea.servicea.type.TaskStatus;
import lombok.Data;

/**
 * @author M.Bezmen
 */
@Data
public class ResultDto {
    private String taskId;
    private String serviceName;
    private TaskStatus status;
}
