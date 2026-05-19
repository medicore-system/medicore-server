package com.medicore.api.dtos.reportes;

import java.math.BigDecimal;

public record EstadoCarteraDTO(
        String codigoEps,
        String nombreEps,
        Long cantidadLiquidaciones,
        BigDecimal deudaTotal
) {
}