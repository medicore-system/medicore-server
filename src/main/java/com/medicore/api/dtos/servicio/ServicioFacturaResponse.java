package com.medicore.api.dtos.servicio;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioFacturaResponse {

    private String codigo;
    private LocalDate fecha;
    private Boolean estado;
    private BigDecimal costoTotal;
    private String descripcion;

    private String codigoCita;
    private String codigoEps;
    private String nombreEps;
    private String codigoHospital;
    private String nombreHospital;
}