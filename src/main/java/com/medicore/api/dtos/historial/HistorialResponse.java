package com.medicore.api.dtos.historial;

import lombok.*;

import java.time.LocalDate;

/**
 * DTO de respuesta para un historial clínico.
 * Incluye los datos básicos del historial más los nombres del paciente y del médico.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialResponse {

    private String codigo;
    private LocalDate fecha;
    private String tipo;
    private String descripcion;
    private String documentoPaciente;
    private String nombrePaciente;
    private String documentoMedico;
    private String nombreMedico;
}
