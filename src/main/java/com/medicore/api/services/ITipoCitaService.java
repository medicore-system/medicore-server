package com.medicore.api.services;

import com.medicore.api.entities.Cita.TipoCita;

import java.util.List;

/**
 * Interfaz de servicio que define las operaciones de gestión de tipos de cita en MediCore.
 */
public interface ITipoCitaService {

    /**
     * Obtiene todos los tipos de citas registrados.
     *
     * @return lista de tipos de citas.
     */
    List<TipoCita> findAll();
}
