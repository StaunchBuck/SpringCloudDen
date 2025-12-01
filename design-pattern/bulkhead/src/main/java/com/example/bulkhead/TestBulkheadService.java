package com.example.bulkhead;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TestBulkheadService {

    private final CountDownLatch latch;
    public Integer process(Integer value){
        System.out.println("Calling any process with value "+value);
        try {
            Thread.sleep(3000);
            latch.countDown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
