package com.example.retry;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalTime;
import java.util.function.Supplier;

@SpringBootApplication
public class RetryApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RetryApplication.class, args);
    }

    static int counter = 0;
    @Override
    public void run(String... args) throws Exception {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)                                // 3 attempts total
                .waitDuration(java.time.Duration.ofMillis(500)) // 500ms wait between retries
                .retryExceptions(RuntimeException.class)      // Retry only these exceptions
                .build();

        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("myRetry");
        Supplier<String> supplier = Retry.decorateSupplier(retry, RetryApplication::unreliableCall);

        try {
            String result = supplier.get();
            System.out.println("Success: " + result);
        } catch (Exception ex) {
            System.out.println("Failed even after retries: " + ex.getMessage());
        }
    }

    static String unreliableCall() {
        counter++;
        System.out.println("Attempt " + counter + " at : "+ LocalTime.now());

        if (counter < 3) {
            throw new RuntimeException("Still failing...");
        }
        return "Success on attempt " + counter;
    }
}
