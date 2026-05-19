package com.medicore.api.dtos.reportes;

import java.math.BigDecimal;

public record IngresosHospitalDTO(
        String codigoHospital,
        String nombreHospital,
        Long cantidadAtenciones,
        BigDecimal ingresosTotales
) {
}