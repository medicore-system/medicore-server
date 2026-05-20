package com.medicore.api.services;

import com.medicore.api.dtos.ciudad.CiudadRequestDTO;
import com.medicore.api.dtos.ciudad.CiudadResponseDTO;
import com.medicore.api.entities.Ciudad;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio que define las operaciones de gestión de ciudades en MediCore.
 */
public interface ICiudadService {

    /**
     * Retorna todas las ciudades activas del sistema.
     *
     * @return lista de ciudades
     */
    List<CiudadResponseDTO> listarCiudades();

    /**
     * Busca ciudades cuyo nombre contenga el texto indicado.
     *
     * @param nombre texto parcial del nombre de la ciudad
     * @return lista de ciudades que coinciden
     */
    List<CiudadResponseDTO> buscarCiudadesPorNombre(String nombre);

    /**
     * Busca una ciudad por su código.
     *
     * @param codigo código de la ciudad
     * @return la ciudad encontrada, o vacío si no existe
     */
    Optional<Ciudad> findById(String codigo);

    /**
     * Obtiene los datos de una ciudad por su código.
     *
     * @param codigo código de la ciudad
     * @return DTO con los datos de la ciudad
     */
    CiudadResponseDTO obtenerCiudad(String codigo);

    /**
     * Crea una nueva ciudad con los datos del request.
     *
     * @param request datos de la ciudad a crear
     * @return DTO de la ciudad creada
     */
    CiudadResponseDTO crearCiudad(CiudadRequestDTO request);

    /**
     * Actualiza los datos de una ciudad existente.
     *
     * @param codigo  código de la ciudad a actualizar
     * @param request nuevos datos de la ciudad
     * @return DTO de la ciudad actualizada
     */
    CiudadResponseDTO editarCiudad(String codigo, CiudadRequestDTO request);

    /**
     * Elimina una ciudad del sistema por su código.
     *
     * @param codigo código de la ciudad a eliminar
     */
    void eliminarCiudad(String codigo);
}
