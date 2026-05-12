package com.medicore.api.services;

import com.medicore.api.entities.TipoCita;

import java.util.List;
import java.util.Optional;
/**
 * Interfaz que define las operaciones de negocio
 * relacionadas con los Tipos de Cita del sistema.
 *
 * <p>Contiene los métodos necesarios para:
 * <ul>
 *     <li>Consultar todos los Tipos de Cita</li>
 * </ul>
 * </p>
 *
 * @author Manuel
 */
public interface ITipoCitaService {

    /**
     * Obtiene todos los Tipos de Cita registrados.
     *
     * @return lista de Tipos de Cita.
     */
    List<TipoCita> findAll();

    /**
     * Busca una EPS por su codigo.
     *
     * @param id codigo del tipo de la cita.
     * @return un Optional con el tipo cinta encontrado,
     * o vacío si no existe.
     */
    Optional<TipoCita> findById(Integer id);
}
