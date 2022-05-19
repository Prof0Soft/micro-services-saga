package com.servicec.payment.controller;

import com.servicec.payment.dto.NewTaskDto;
import com.servicec.payment.dto.TaskInfoDto;
import com.servicec.payment.dto.TaskStatusDto;
import com.servicec.payment.service.TaskService;
import org.springframework.web.bind.annotation.*;

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
