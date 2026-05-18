package com.medicore.api.dtos.tarifa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TarifaEpsResponseDTO(
        String codigo,
        String codigoEps,
        String nombreEps,
        String codigoServicio,
        String nombreServicio,
        BigDecimal porcentajeCobertura,
        Boolean estado,
        LocalDateTime fechaCreacion
) {
}