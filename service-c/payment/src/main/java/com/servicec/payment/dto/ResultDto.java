package com.servicec.payment.dto;

import com.servicec.payment.type.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@AllArgsConstructor
@Data
public class ResultDto {
    private UUID taskId;
    private String serviceName;
    private TaskStatus status;
}
