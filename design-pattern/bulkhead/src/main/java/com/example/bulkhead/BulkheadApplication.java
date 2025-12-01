package com.example.bulkhead;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Function;

@SpringBootApplication
public class BulkheadApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BulkheadApplication.class, args);
	}

    @Autowired
    private CountDownLatch latch;

    @Override
    public void run(String... args) throws Exception {

//        We can configure the following settings:
//           1) the maximum amount of parallel executions allowed by the bulkhead
//           2) the maximum amount of time a thread will wait when attempting to enter a saturated bulkhead

        BulkheadConfig config = BulkheadConfig.custom().maxConcurrentCalls(1).build();
        BulkheadRegistry registry = BulkheadRegistry.of(config);
        Bulkhead bulkhead = registry.bulkhead("my-bulkhead");
        TestBulkheadService service = new TestBulkheadService(latch);
        Function<Integer, Integer> decorated
                = Bulkhead.decorateFunction(bulkhead, service::process);

        ForkJoinTask<?> task = ForkJoinPool.commonPool().submit(() -> {
            try {
                decorated.apply(21);
            } finally {
                System.out.println("bulkhead complete");
                bulkhead.onComplete();
            }
        });
        latch.await();
        System.out.println("Trying to acquire new connection after latch await : "+!bulkhead.tryAcquirePermission());
    }
}

@Configuration
class Config{

    @Bean
    public CountDownLatch countDownLatch(){
        return new CountDownLatch(1);
    }
}