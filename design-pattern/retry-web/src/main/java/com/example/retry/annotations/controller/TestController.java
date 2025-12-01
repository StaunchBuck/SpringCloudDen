package com.example.retry.annotations.controller;

import com.example.retry.annotations.service.MyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    private final MyService myService;

    public TestController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("api/test-retry")
    public String testRetry() {
        return myService.getRemoteData();
    }
}

