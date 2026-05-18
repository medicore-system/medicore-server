package com.medicore.api.dtos.liquidacion;

import com.medicore.api.dtos.factura.FacturaResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record LiquidacionResponseDTO(
        String codigo,
        String codigoEps,
        String nombreEps,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        BigDecimal totalBruto,
        BigDecimal totalCoberturaEps,
        BigDecimal totalCopagoPaciente,
        String estado,
        LocalDateTime fechaGeneracion,
        List<FacturaResponseDTO> facturas
) {
}