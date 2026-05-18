package com.medicore.api.mappers;

import com.medicore.api.dtos.factura.FacturaResponseDTO;
import com.medicore.api.dtos.liquidacion.LiquidacionResponseDTO;
import com.medicore.api.entities.Factura;
import com.medicore.api.entities.costos.Liquidacion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LiquidacionMapper {

    public LiquidacionResponseDTO toResponseDTO(Liquidacion liquidacion) {
        if (liquidacion == null) {
            return null;
        }

        List<FacturaResponseDTO> facturasResponse = liquidacion.getFacturas().stream()
                .map(this::mapFacturaToDTO)
                .collect(Collectors.toList());

        return new LiquidacionResponseDTO(
                liquidacion.getCodigo(),
                liquidacion.getEps().getCodigo(),
                liquidacion.getEps().getNombre(),
                liquidacion.getFechaInicio(),
                liquidacion.getFechaFin(),
                liquidacion.getTotalBruto(),
                liquidacion.getTotalCoberturaEps(),
                liquidacion.getTotalCopagoPaciente(),
                liquidacion.getEstado(),
                liquidacion.getFechaGeneracion(),
                facturasResponse
        );
    }

    private FacturaResponseDTO mapFacturaToDTO(Factura factura) {
        return new FacturaResponseDTO(
                factura.getCodigo(),
                factura.getFecha(),
                factura.getEstado(),
                factura.getCostoTotal(),
                factura.getDescripcion(),
                factura.getCita().getCodigo(),
                factura.getEps().getCodigo(),
                factura.getEps().getNombre(),
                factura.getHospital().getCodigo(),
                factura.getHospital().getNombre(),
                factura.getLiquidacion() != null ? factura.getLiquidacion().getCodigo() : null
        );
    }
}