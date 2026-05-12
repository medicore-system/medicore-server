package com.medicore.api.dtos.servicio;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioDetalleResponse {

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

    private ServicioHistorialResponse historialClinico;
    private List<ServicioFacturaResponse> facturas;
}