package com.medicore.api.services;

import java.util.List;

import com.medicore.api.dtos.liquidacion.LiquidacionRequestDTO;
import com.medicore.api.dtos.liquidacion.LiquidacionResponseDTO;

/**
 * Interfaz de servicio que define las operaciones de gestión de liquidaciones en MediCore.
 */
public interface ILiquidacionService {

    /**
     * Genera una nueva liquidación consolidando facturas de una EPS en un período.
     *
     * @param request datos para generar la liquidación
     * @return DTO de la liquidación generada
     */
    LiquidacionResponseDTO generarLiquidacion(LiquidacionRequestDTO request);

    /**
     * Obtiene el historial completo de liquidaciones del sistema.
     *
     * @return lista de todas las liquidaciones
     */
    List<LiquidacionResponseDTO> obtenerTodas();

    /**
     * Cambia el estado de una liquidación (ej: PENDIENTE → APROBADA).
     *
     * @param codigo       código de la liquidación
     * @param nuevoEstado  nuevo estado a asignar
     * @return DTO de la liquidación con el estado actualizado
     */
    LiquidacionResponseDTO cambiarEstado(String codigo, String nuevoEstado);

    /**
     * Genera el archivo PDF de una liquidación.
     *
     * @param codigo código de la liquidación
     * @return arreglo de bytes del PDF generado
     */
    byte[] generarPdfLiquidacion(String codigo);
}