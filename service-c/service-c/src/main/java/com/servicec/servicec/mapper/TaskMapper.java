package com.servicec.servicec.mapper;

import com.servicec.servicec.dto.TaskInfoDto;
import com.servicec.servicec.dto.TaskStatusDto;
import com.servicec.servicec.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author M.Bezmen
 */
@Mapper
public interface TaskMapper {
    @Mapping(target = "id", expression = "java(task.getId().toString())")
    TaskInfoDto toDto(Task task);

    @Mapping(target = "id", expression = "java(task.getId().toString())")
    TaskStatusDto toStatusDto(Task task);
}
