package com.medicore.api.dtos.cita;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaSlotResponseDTO {
    // --- Lo que el frontend muestra en la tarjeta ---

    /** "Martes 27 de mayo - 07:30" */
    private String fechaDisplay;

    /** Nombre completo del médico */
    private String nombreMedico;

    /** Nombre del hospital */
    private String nombreHospital;

    /** Ciudad del hospital */
    private String nombreCiudad;

    private BigDecimal costo;

    // --- Lo que el frontend envía en el POST /Citas ---

    /** Fecha/hora exacta para el campo fecha del POST */
    private LocalDateTime fecha;
    private String documentoMedico;
    private String codigoHospital;
    private Integer idEspecialidad;
    private Integer idTipo;
}
