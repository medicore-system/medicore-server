package com.medicore.api.dtos.liquidacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LiquidacionRequestDTO(
        @NotBlank(message = "El código de la EPS es obligatorio")
        String codigoEps,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin
) {
}