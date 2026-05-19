package com.medicore.api.services;

import com.medicore.api.dtos.tarifa.TarifaEpsRequestDTO;
import com.medicore.api.dtos.tarifa.TarifaEpsResponseDTO;

import java.util.List;

public interface ITarifaEpsService {
    TarifaEpsResponseDTO crearTarifa(TarifaEpsRequestDTO request);
    List<TarifaEpsResponseDTO> obtenerTarifasActivasPorEps(String codigoEps);
    TarifaEpsResponseDTO actualizarTarifa(String codigo, TarifaEpsRequestDTO request);
}