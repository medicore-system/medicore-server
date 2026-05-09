package com.medicore.api.dtos;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDetailResponse {

    private String codigo;
    private String nombre;
    private String direccion;
    private String telefono;
    private Boolean estado;
    private String codigoCiudad;
    private String nombreCiudad;
    private List<AreaInternaResponse> areasInternas;
}