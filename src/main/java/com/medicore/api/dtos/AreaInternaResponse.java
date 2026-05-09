package com.medicore.api.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaInternaResponse {

    private String codigo;
    private String nombre;
    private String descripcion;
    private String codigoAreaInterna;
    private String nombreAreaInterna;
}