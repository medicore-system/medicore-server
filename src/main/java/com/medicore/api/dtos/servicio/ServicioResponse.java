package com.medicore.api.dtos.servicio;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioResponse {

    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer idTipoServicio;
    private String tipo;
    private BigDecimal precio;
    private Boolean estado;
    private String procedimiento;
    private String resultados;
    private String codigoHistorial;
}