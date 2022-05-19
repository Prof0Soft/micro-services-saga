package com.serviceb.storeroom.dto;

import com.serviceb.storeroom.type.TaskStatus;
import lombok.Data;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@Data
public class TaskStatusDto {
    private UUID id;
    private TaskStatus status;
}
