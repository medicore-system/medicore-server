package com.medicore.api.dtos.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponseDTO {
    private String documento;
    private String nombre;
    private String apellido;
    private Integer idEspecialidad;
    private String telefono;
    private String email;
    private String status;
    private String codigoCiudad;

}