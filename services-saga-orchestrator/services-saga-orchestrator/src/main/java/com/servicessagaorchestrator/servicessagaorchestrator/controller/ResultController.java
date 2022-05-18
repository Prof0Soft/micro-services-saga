package com.servicessagaorchestrator.servicessagaorchestrator.controller;

import com.servicessagaorchestrator.servicessagaorchestrator.dto.ResultDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author M.Bezmen
 */
@RestController
public class ResultController {

    @PostMapping("/result")
    public void submitTaskResult(@RequestBody ResultDto result) {

    }
}
