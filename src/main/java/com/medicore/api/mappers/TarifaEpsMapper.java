package com.medicore.api.mappers;

import com.medicore.api.dtos.tarifa.TarifaEpsResponseDTO;
import com.medicore.api.entities.costos.TarifaEps;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TarifaEpsMapper {

    @Mapping(source = "eps.codigo", target = "codigoEps")
    @Mapping(source = "eps.nombre", target = "nombreEps")
    @Mapping(source = "servicio.codigo", target = "codigoServicio")
    @Mapping(source = "servicio.nombre", target = "nombreServicio")
    TarifaEpsResponseDTO toResponseDTO(TarifaEps tarifaEps);
}