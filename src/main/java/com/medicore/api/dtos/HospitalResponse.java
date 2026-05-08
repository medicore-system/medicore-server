package com.medicore.api.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponse {

    private String codigo;
    private String nombre;
    private String direccion;
    private String telefono;
    private Boolean estado;
    private String codigoCiudad;
}
