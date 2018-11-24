package com.rest.offerservice.healthcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired
    HealthCheckService healthCheckService;

    @GetMapping("/health")
    public String checkHealth(){
        return healthCheckService.checkHealth();
    }
}
