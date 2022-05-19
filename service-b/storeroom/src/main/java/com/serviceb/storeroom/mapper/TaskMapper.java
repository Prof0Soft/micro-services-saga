package com.serviceb.storeroom.mapper;

import com.serviceb.storeroom.dto.ResultDto;
import com.serviceb.storeroom.dto.TaskInfoDto;
import com.serviceb.storeroom.dto.TaskStatusDto;
import com.serviceb.storeroom.entity.Task;
import org.mapstruct.Mapper;

/**
 * @author M.Bezmen
 */
@Mapper
public interface TaskMapper {

    ResultDto toResultDto(Task task);

    TaskInfoDto toInfoDto(Task task);
    TaskStatusDto toStatusDto(Task task);

}
