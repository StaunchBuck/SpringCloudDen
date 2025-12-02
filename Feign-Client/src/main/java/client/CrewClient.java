package client;

import config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "crew-service",
        url = "http://localhost:8081",
        configuration = FeignClientConfig.class
)
public interface CrewClient {

    @GetMapping("/api/crew/{id}")
    String getCrew(@PathVariable String id);
}
