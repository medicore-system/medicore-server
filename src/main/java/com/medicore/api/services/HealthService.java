package com.medicore.api.services;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthService {

    public Map<String, String> checkSystemStatus(String ciudad) {
        Map<String, String> status = new HashMap<>();
        status.put("sistema", "MediCore API");
        status.put("estado", "OPERACIONAL");
        status.put("nodo", ciudad.toUpperCase());
        status.put("mensaje", "Cimientos de la estructura tradicional listos para el MVP.");
        return status;
    }
}