package com.servicea.servicea.mapper;

import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.dto.TaskStatusDto;
import com.servicea.servicea.entity.Task;
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
