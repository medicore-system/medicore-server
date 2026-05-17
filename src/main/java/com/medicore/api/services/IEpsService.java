package com.medicore.api.services;

import com.medicore.api.entities.Eps;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de negocio
 * relacionadas con los eps del sistema.
 *
 * <p>Contiene los métodos necesarios para:
 * <ul>
 *     <li>Consultar todas las Eps</li>
 * </ul>
 * </p>
 *
 * @author Manuel
 */
public interface IEpsService {

    /**
     * Obtiene todas las Eps registradas.
     *
     * @return lista de Eps.
     */
    List<Eps> findAll();

    /**
     * Busca una EPS por su codigo.
     *
     * @param codigo codigo de la EPS.
     * @return un Optional con la EPS encontrada,
     * o vacío si no existe.
     */
    Optional<Eps> findById(String codigo);
}
