package com.medicore.api.dtos.AsignacionMedico;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AsignacionMedicoResponseDTO {
    private Integer codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String nombreMedico;
    private String nombreCiudad;
    private String nombreHospital;
    private String horario;

}
