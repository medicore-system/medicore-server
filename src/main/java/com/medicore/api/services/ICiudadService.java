package com.medicore.api.services;

import com.medicore.api.dtos.ciudad.CiudadRequestDTO;
import com.medicore.api.dtos.ciudad.CiudadResponseDTO;
import com.medicore.api.entities.Ciudad;

import java.util.List;
import java.util.Optional;

public interface ICiudadService {

    List<CiudadResponseDTO> listarCiudades();

    List<CiudadResponseDTO> buscarCiudadesPorNombre(String nombre);

    Optional<Ciudad> findById(String codigo);

    CiudadResponseDTO obtenerCiudad(String codigo);

    CiudadResponseDTO crearCiudad(CiudadRequestDTO request);

    CiudadResponseDTO editarCiudad(String codigo, CiudadRequestDTO request);

    void eliminarCiudad(String codigo);
}
