package com.medicore.api.services;

import com.medicore.api.dtos.CiudadRequest;
import com.medicore.api.dtos.CiudadResponse;

import java.util.List;

public interface ICiudadService {

    List<CiudadResponse> listarCiudades();

    List<CiudadResponse> buscarCiudadesPorNombre(String nombre);

    CiudadResponse obtenerCiudad(String codigo);

    CiudadResponse crearCiudad(CiudadRequest request);

    CiudadResponse editarCiudad(String codigo, CiudadRequest request);

    void eliminarCiudad(String codigo);
}
