package com.example.ratelimiter;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalTime;
import java.util.function.Supplier;

@SpringBootApplication
public class RateLimiterApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RateLimiterApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

//        All calls on the decorated service block confirm, if necessary, to the rate limiter configuration.
//        We can configure parameters like:
//          1) the period of the limit refresh
//          2) the permissions limit for the refresh period
//          3) the default wait for permission duration

        RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(java.time.Duration.ofMillis(500))   // wait max 500ms for permission
                .limitRefreshPeriod(java.time.Duration.ofSeconds(2)) // refresh every 2 sec
                .limitForPeriod(5)                                   // allow 5 calls per second
                .build();

        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiter = registry.rateLimiter("myRateLimiter");

        // Decorate a supplier function
        Supplier<String> limitedCall = RateLimiter
                .decorateSupplier(rateLimiter, RateLimiterApplication::callExternalService);

        // Execute the call
        for (int i=1;i <= 10;i++) {
            try {
                System.out.println(limitedCall.get());
            } catch (Exception e) {
                System.out.println("Request " + i + " blocked: " + e.getMessage());
            }
        }

    }

    static String callExternalService() {
        return "Success - " + LocalTime.now();
    }
}