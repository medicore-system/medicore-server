package com.medicore.api.dtos.servicio;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoServicioResponse {

    private Integer id;
    private String nombre;
    private String prefijo;
}