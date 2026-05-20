package com.medicore.api.controllers;

import com.medicore.api.services.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Controlador REST para verificar el estado del sistema MediCore.
 * Expone un endpoint de salud para monitoreo y diagnóstico.
 */
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    private final HealthService healthService;

    /**
     * Constructor que inyecta el servicio de salud del sistema.
     *
     * @param healthService servicio que verifica el estado del sistema
     */
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    /**
     * Verifica el estado general del sistema en la ciudad indicada.
     *
     * @param ciudad nombre de la ciudad a verificar (por defecto "Manizales")
     * @return mapa con claves y valores que describen el estado del sistema
     */
    @GetMapping("/check")
    public Map<String, String> check(@RequestParam(defaultValue = "Manizales") String ciudad) {
        return healthService.checkSystemStatus(ciudad);
    }
}