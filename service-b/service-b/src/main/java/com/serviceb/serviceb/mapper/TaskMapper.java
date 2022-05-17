package com.serviceb.serviceb.mapper;

import com.serviceb.serviceb.dto.TaskInfoDto;
import com.serviceb.serviceb.dto.TaskStatusDto;
import com.serviceb.serviceb.entity.Task;
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
