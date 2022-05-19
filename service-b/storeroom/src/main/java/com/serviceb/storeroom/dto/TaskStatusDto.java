package com.serviceb.storeroom.dto;

import com.serviceb.storeroom.type.TaskStatus;
import lombok.Data;

/**
 * @author M.Bezmen
 */
@Data
public class TaskStatusDto {
    private String id;
    private TaskStatus status;
}
