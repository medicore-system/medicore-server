package com.medicore.api.services;

import com.medicore.api.dtos.tarifa.TarifaEpsRequestDTO;
import com.medicore.api.dtos.tarifa.TarifaEpsResponseDTO;

import java.util.List;

/**
 * Interfaz de servicio que define las operaciones de gestión de tarifas EPS en MediCore.
 */
public interface ITarifaEpsService {

    /**
     * Crea una nueva tarifa de cobertura EPS para un servicio.
     *
     * @param request datos de la tarifa a crear
     * @return DTO de la tarifa creada
     */
    TarifaEpsResponseDTO crearTarifa(TarifaEpsRequestDTO request);

    /**
     * Obtiene todas las tarifas activas de una EPS.
     *
     * @param codigoEps código de la EPS
     * @return lista de tarifas activas
     */
    List<TarifaEpsResponseDTO> obtenerTarifasActivasPorEps(String codigoEps);

    /**
     * Actualiza una tarifa EPS existente.
     *
     * @param codigo  código de la tarifa a actualizar
     * @param request nuevos datos de la tarifa
     * @return DTO de la tarifa actualizada
     */
    TarifaEpsResponseDTO actualizarTarifa(String codigo, TarifaEpsRequestDTO request);
}