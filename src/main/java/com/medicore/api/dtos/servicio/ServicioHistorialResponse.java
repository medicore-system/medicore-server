package com.medicore.api.dtos.servicio;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioHistorialResponse {

    private String codigo;
    private LocalDate fecha;
    private String tipo;
    private String descripcion;

    private String documentoPaciente;
    private String nombrePaciente;

    private String documentoMedico;
    private String nombreMedico;
}