package com.example.retry.annotations.service;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @Retry(name = "myRetry", fallbackMethod = "retryFallback")
    public String getRemoteData() {
        System.out.println("Calling remote API...");
        throw new RuntimeException("Remote service failed!");
    }

    public String retryFallback(Exception ex) {
        return "Fallback response: " + ex.getMessage();
    }
}
