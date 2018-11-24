package com.rest.offerservice.healthcheck;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
public class HealthCheckService {

    public String checkHealth(){
        return "Application is OK";
    }
}
