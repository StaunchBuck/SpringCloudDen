package com.example.timeLimiter.controller;

import com.example.timeLimiter.service.TimeLimiterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class TestController {

    private final TimeLimiterService service;

    public TestController(TimeLimiterService service) {
        this.service = service;
    }

    @GetMapping("/data")
    public CompletableFuture<String> data() {
        return service.getData();
    }
}
