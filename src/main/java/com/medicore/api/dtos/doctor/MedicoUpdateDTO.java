package com.medicore.api.dtos.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoUpdateDTO {
    private String nombre;
    private String apellido;
    private Integer idEspecialidad;
    private String telefono;
    private String email;
    private String codigoCiudad;
}
