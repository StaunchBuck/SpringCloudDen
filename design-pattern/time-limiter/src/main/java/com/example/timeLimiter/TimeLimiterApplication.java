package com.example.timeLimiter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class TimeLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeLimiterApplication.class, args);
    }

    public void run(String... args) throws Exception {
        SpringApplication.run(TimeLimiterApplication.class);
    }
}
