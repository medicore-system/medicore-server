package com.medicore.api.dtos.doctor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//Request --> Lo que el frontend envia(la info que enviamos)
public class MedicoRequestDTO {
    private String documento;
    private String nombre;
    private String apellido;
    private Integer idEspecialidad;
    private String telefono;
    private String email;
    private String codigoCiudad;
    private Boolean estado;

}