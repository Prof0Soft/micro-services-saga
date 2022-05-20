package com.servicea.order.dto;

import com.servicea.order.type.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultDto {
    private UUID taskId;
    private String serviceName;
    private TaskStatus status;
}
