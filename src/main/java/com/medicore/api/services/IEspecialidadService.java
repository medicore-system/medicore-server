package com.medicore.api.services;

import com.medicore.api.entities.Especialidad;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio encargada de definir
 * las operaciones relacionadas con las especialidades.
 */
public interface IEspecialidadService {

    /**
     * Obtiene la lista de todas las especialidades registradas.
     *
     * @return lista de especialidades
     */
    List<Especialidad> findAll();

    /**
     * Busca una especialidad por su identificador.
     *
     * @param id identificador de la especialidad
     * @return especialidad encontrada o vacío si no existe
     */
    Optional<Especialidad> findById(Integer id);
}