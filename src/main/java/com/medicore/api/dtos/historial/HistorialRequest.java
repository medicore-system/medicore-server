package com.medicore.api.dtos.historial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO que contiene los datos necesarios para registrar un historial clínico.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialRequest {

    @NotBlank(message = "El código del historial es obligatorio")
    @Size(max = 50, message = "El código no puede superar 50 caracteres")
    private String codigo;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El tipo de atención es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar 50 caracteres")
    private String tipo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 200, message = "La descripción no puede superar 200 caracteres")
    private String descripcion;

    @NotBlank(message = "El documento del paciente es obligatorio")
    private String documentoPaciente;

    @NotBlank(message = "El documento del médico es obligatorio")
    private String documentoMedico;
}
