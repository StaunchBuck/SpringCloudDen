package com.example.timeLimiter.service;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TimeLimiterService {

    @TimeLimiter(name = "myTimeLimiter", fallbackMethod = "fallback")
    public CompletableFuture<String> getData() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("sleep start");
                Thread.sleep(5000);
                System.out.println("sleep end");
            } catch (Exception ignored) {
                System.out.println("exception");
                ignored.printStackTrace();
            }
            return "Success!";
        });
    }

    public CompletableFuture<String> fallback(Throwable t) {
        return CompletableFuture.completedFuture("Fallback: Request timed out");
    }
}
