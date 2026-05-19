package com.medicore.api.dtos.reportes;

import java.math.BigDecimal;

public record AtencionesEpsDTO(
        String codigoEps,
        String nombreEps,
        Long totalAtenciones,
        BigDecimal totalFacturado
) {
}