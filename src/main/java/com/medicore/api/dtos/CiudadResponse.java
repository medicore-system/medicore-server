package com.medicore.api.dtos;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CiudadResponse {

    private String codigo;
    private String nombre;
    private String departamento;
    private Long totalHospitales;
}
