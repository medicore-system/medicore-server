package com.medicore.api.mappers;

import com.medicore.api.dtos.CiudadResponse;
import com.medicore.api.entities.Ciudad;
import org.springframework.stereotype.Component;

@Component
public class CiudadMapper {

    public CiudadResponse toResponse(Ciudad ciudad, Long totalHospitales) {
        return CiudadResponse.builder()
                .codigo(ciudad.getCodigo())
                .nombre(ciudad.getNombre())
                .departamento(ciudad.getDepartamento().getNombre())
                .totalHospitales(totalHospitales)
                .build();
    }
}
