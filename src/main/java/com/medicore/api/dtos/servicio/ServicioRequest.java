package com.medicore.api.dtos.servicio;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioRequest {

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 50, message = "El nombre del servicio no puede superar los 50 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción del servicio es obligatoria")
    @Size(max = 250, message = "La descripción del servicio no puede superar los 250 caracteres")
    private String descripcion;

    @NotNull(message = "El tipo de servicio es obligatorio")
    private Integer idTipoServicio;

    @NotNull(message = "El precio del servicio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a cero")
    private BigDecimal precio;

    @Size(max = 250, message = "El procedimiento no puede superar los 250 caracteres")
    private String procedimiento;

    @Size(max = 250, message = "Los resultados no pueden superar los 250 caracteres")
    private String resultados;

    @Size(max = 50, message = "El código de historial clínico no puede superar los 50 caracteres")
    private String codigoHistorial;

    private Boolean estado;
}