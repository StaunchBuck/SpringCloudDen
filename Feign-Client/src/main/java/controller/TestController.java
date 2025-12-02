package controller;

import client.CrewClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private CrewClient crewClient;

    @GetMapping("/test")
    public String test() {
        return crewClient.getCrew("10");
    }
}

