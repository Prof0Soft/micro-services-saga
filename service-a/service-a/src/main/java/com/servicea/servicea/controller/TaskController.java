package com.servicea.servicea.controller;

import com.servicea.servicea.dto.NewTaskDto;
import com.servicea.servicea.dto.TaskInfoDto;
import com.servicea.servicea.dto.TaskStatusDto;
import com.servicea.servicea.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskInfoDto createTask(@RequestBody final NewTaskDto task) {
        return taskService.createTask(task);
    }

    @GetMapping("/{taskId}/status")
    public TaskStatusDto getTaskStatus(@PathVariable final UUID taskId) {
        return taskService.getTaskStatus(taskId);
    }

    @PostMapping("/{taskId}/cancel")
    public TaskStatusDto cancelTask(@PathVariable final UUID taskId) {
        return taskService.cancelTask(taskId);
    }

    @PostMapping("/{taskId}/revert")
    public TaskStatusDto revertTask(@PathVariable final UUID taskId) {
        return taskService.revertTask(taskId);
    }
}
