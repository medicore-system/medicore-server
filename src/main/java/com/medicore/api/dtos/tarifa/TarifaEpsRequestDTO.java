package com.medicore.api.dtos.tarifa;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TarifaEpsRequestDTO(
        
        @NotBlank(message = "El código de la EPS es obligatorio")
        String codigoEps,

        @NotBlank(message = "El código del servicio es obligatorio")
        String codigoServicio,

        @NotNull(message = "El porcentaje de cobertura no puede ser nulo")
        @DecimalMin(value = "0.00", message = "El porcentaje no puede ser menor a 0")
        @DecimalMax(value = "100.00", message = "El porcentaje no puede ser mayor a 100")
        BigDecimal porcentajeCobertura
) {
}