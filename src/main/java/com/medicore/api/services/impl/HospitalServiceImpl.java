package com.medicore.api.services.impl;

import com.medicore.api.dtos.HospitalRequest;
import com.medicore.api.dtos.HospitalResponse;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Hospital;
import com.medicore.api.repositories.CiudadRepository;
import com.medicore.api.repositories.HospitalRepository;
import com.medicore.api.services.IHospitalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements IHospitalService {

    private final HospitalRepository hospitalRepository;
    private final CiudadRepository ciudadRepository;

    @Override
    @Transactional
    public HospitalResponse createHospital(HospitalRequest request) {
        Ciudad ciudad = ciudadRepository.findById(request.getCodigoCiudad())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ciudad no encontrada con código: " + request.getCodigoCiudad()));

        Hospital hospital = Hospital.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .estado(true)
                .ciudad(ciudad)
                .build();

        Hospital saved = hospitalRepository.save(hospital);
        return toResponse(saved);
    }

    private HospitalResponse toResponse(Hospital hospital) {
        return HospitalResponse.builder()
                .codigo(hospital.getCodigo())
                .nombre(hospital.getNombre())
                .direccion(hospital.getDireccion())
                .telefono(hospital.getTelefono())
                .estado(hospital.getEstado())
                .codigoCiudad(hospital.getCiudad().getCodigo())
                .build();
    }
}
