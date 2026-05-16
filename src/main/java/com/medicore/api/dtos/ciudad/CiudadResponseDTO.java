package com.medicore.api.dtos.ciudad;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CiudadResponseDTO {

    private String codigo;
    private String nombre;
    private String departamento;
    private Long totalHospitales;
}
