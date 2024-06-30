package com.hasanmutlu.articles.integrationflow.integrationflow.controllers;

import com.hasanmutlu.articles.integrationflow.integrationflow.dto.LogDto;
import com.hasanmutlu.articles.integrationflow.integrationflow.services.ILogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {

    private final ILogService logService;

    @PostMapping
    public ResponseEntity<String> addLogMessages(@RequestBody LogDto log) {
        CompletableFuture.runAsync(() -> logService.sendLogMessages(log));
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> addLogMessages(@RequestBody List<LogDto> logs) {
        CompletableFuture.runAsync(() -> logService.sendLogMessages(logs));
        return ResponseEntity.ok("OK");
    }
}
