package com.medicore.api.services;

import com.medicore.api.entities.Cita.TipoCita;

import java.util.List;

public interface ITipoCitaService {

    /**
     * Obtiene todos los tipos de citas registrados.
     *
     * @return lista de tipos de citas.
     */
    List<TipoCita> findAll();
}
