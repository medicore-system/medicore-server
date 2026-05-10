package com.medicore.api.services;

import com.medicore.api.dtos.CiudadRequest;
import com.medicore.api.dtos.CiudadResponse;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Medico;

import java.util.List;
import java.util.Optional;

public interface ICiudadService {

    List<CiudadResponse> listarCiudades();

    List<CiudadResponse> buscarCiudadesPorNombre(String nombre);
    Optional<Ciudad> findById(String codigo);

    CiudadResponse obtenerCiudad(String codigo);

    CiudadResponse crearCiudad(CiudadRequest request);

    CiudadResponse editarCiudad(String codigo, CiudadRequest request);

    void eliminarCiudad(String codigo);
}
