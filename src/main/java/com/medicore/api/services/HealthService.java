package com.medicore.api.services;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para verificar el estado operacional del sistema MediCore.
 * Retorna información de diagnóstico sobre el nodo activo y el estado general.
 */
@Service
public class HealthService {

    /**
     * Verifica el estado del sistema en la ciudad indicada.
     *
     * @param ciudad nombre de la ciudad del nodo a verificar
     * @return mapa con claves "sistema", "estado", "nodo" y "mensaje"
     */
    public Map<String, String> checkSystemStatus(String ciudad) {
        Map<String, String> status = new HashMap<>();
        status.put("sistema", "MediCore API");
        status.put("estado", "OPERACIONAL");
        status.put("nodo", ciudad.toUpperCase());
        status.put("mensaje", "Cimientos de la estructura tradicional listos para el MVP.");
        return status;
    }
}