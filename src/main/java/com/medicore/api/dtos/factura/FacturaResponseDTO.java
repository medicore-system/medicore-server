package com.medicore.api.dtos.factura;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FacturaResponseDTO(
        String codigo,
        LocalDate fecha,
        Boolean estado,
        BigDecimal costoTotal,
        String descripcion,
        String codigoCita,
        String codigoEps,
        String nombreEps,             
        String codigoHospital,
        String nombreHospital,
        String codigoLiquidacion
) {
}