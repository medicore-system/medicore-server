package com.medicore.api.dtos.reportes;

import java.math.BigDecimal;

public record IngresosEspecialidadDTO(
        Integer idEspecialidad,
        String nombreEspecialidad,
        Long cantidadAtenciones,
        BigDecimal ingresosTotales
) {
}