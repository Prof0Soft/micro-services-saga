package com.servicessagaorchestrator.servicessagaorchestrator.service.client;

import com.servicessagaorchestrator.servicessagaorchestrator.config.McCClientConfig;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskInfoDto;
import com.servicessagaorchestrator.servicessagaorchestrator.dto.TaskStatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * @author M.Bezmen
 */
@FeignClient(name = "mc-c-client", configuration = McCClientConfig.class, url = "${client.mcc.host}")
public interface McCClient extends StartProcessClientService {

    @PostMapping("/tasks")
    TaskInfoDto createTask(@RequestBody ResultDto taskId);

    @GetMapping("/tasks/{taskId}/status")
    TaskStatusDto getTaskStatus(@PathVariable final UUID taskId);

    @PostMapping("/tasks/{taskId}/cancel")
    TaskStatusDto cancelTask(@PathVariable final UUID taskId);

    @PostMapping("/tasks/{taskId}/revert")
    TaskStatusDto revertTask(@PathVariable final UUID taskId);
}
