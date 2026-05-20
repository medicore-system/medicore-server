package com.medicore.api.services;

import java.util.List;

import com.medicore.api.dtos.liquidacion.LiquidacionRequestDTO;
import com.medicore.api.dtos.liquidacion.LiquidacionResponseDTO;

public interface ILiquidacionService {
    LiquidacionResponseDTO generarLiquidacion(LiquidacionRequestDTO request);
    List<LiquidacionResponseDTO> obtenerTodas();
}