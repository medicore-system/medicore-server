package com.medicore.api.controllers;

import com.medicore.api.services.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/check")
    public Map<String, String> check(@RequestParam(defaultValue = "Manizales") String ciudad) {
        return healthService.checkSystemStatus(ciudad);
    }
}