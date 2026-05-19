package com.medicore.api.mappers;

import com.medicore.api.dtos.tarifa.TarifaEpsResponseDTO;
import com.medicore.api.entities.costos.TarifaEps;
import org.springframework.stereotype.Component;

@Component
public class TarifaEpsMapper {

    public TarifaEpsResponseDTO toResponseDTO(TarifaEps tarifaEps) {
        if (tarifaEps == null) {
            return null;
        }

        return new TarifaEpsResponseDTO(
                tarifaEps.getCodigo(),
                tarifaEps.getEps().getCodigo(),
                tarifaEps.getEps().getNombre(),
                tarifaEps.getServicio().getCodigo(),
                tarifaEps.getServicio().getNombre(),
                tarifaEps.getPorcentajeCobertura(),
                tarifaEps.getEstado(),
                tarifaEps.getFechaCreacion()
        );
    }
}