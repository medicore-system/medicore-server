package com.medicore.api.dtos.factura;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FacturaCajaDTO(
        String codigoFactura,
        String nombrePaciente,
        String descripcionServicio,
        LocalDate fecha,
        String nombreEps,
        BigDecimal costoTotal,
        BigDecimal coberturaEps,
        BigDecimal copagoAPagar,
        Boolean estaPagado
) {}