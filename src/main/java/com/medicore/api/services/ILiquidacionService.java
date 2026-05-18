package com.medicore.api.services;

import com.medicore.api.dtos.liquidacion.LiquidacionRequestDTO;
import com.medicore.api.dtos.liquidacion.LiquidacionResponseDTO;

public interface ILiquidacionService {
    LiquidacionResponseDTO generarLiquidacion(LiquidacionRequestDTO request);
}