package com.medicore.api.services;

import com.medicore.api.dtos.historial.HistorialRequest;
import com.medicore.api.dtos.historial.HistorialResponse;

import java.util.List;

/**
 * Contrato del servicio de gestión de historiales clínicos.
 */
public interface IHistorialService {

    /**
     * Crea un nuevo historial clínico.
     *
     * @param request datos del historial a registrar.
     * @return historial creado.
     */
    HistorialResponse crearHistorial(HistorialRequest request);

    /**
     * Lista todos los historiales clínicos registrados.
     *
     * @return lista de historiales.
     */
    List<HistorialResponse> listarHistoriales();
}
