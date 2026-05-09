package com.medicore.api.services.impl;

import com.medicore.api.dtos.*;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Hospital;
import com.medicore.api.repositories.CiudadRepository;
import com.medicore.api.repositories.HospitalRepository;
import com.medicore.api.services.IHospitalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements IHospitalService {

    private final HospitalRepository hospitalRepository;
    private final CiudadRepository ciudadRepository;

    @Override
    @Transactional
    public HospitalResponse createHospital(HospitalRequest request) {
        Ciudad ciudad = validarCiudad(request.getCodigoCiudad());
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

    @Override
    @Transactional
    public HospitalResponse updateHospital(HospitalUpdateRequest request, String id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hospital no encontrado con código: " + id));
        Ciudad ciudad = validarCiudad(request.getCodigoCiudad());
        Hospital hospitalBuilder = Hospital.builder()
                .codigo(id)
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .estado(request.getEstado())
                .ciudad(ciudad)
                .build();

        Hospital saved = hospitalRepository.save(hospitalBuilder);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospitalResponse> getAllHospitals() {
        return hospitalRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HospitalDetailResponse getHospitalByCodigo(String codigo) {
        Hospital hospital = hospitalRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hospital no encontrado con código: " + codigo));

        List<AreaInternaResponse> areas = hospital.getAreasInternas()
                .stream()
                .map(hai -> AreaInternaResponse.builder()
                        .codigo(hai.getCodigo())
                        .nombre(hai.getNombre())
                        .descripcion(hai.getDescripcion())
                        .codigoAreaInterna(hai.getAreaInterna().getCodigo())
                        .nombreAreaInterna(hai.getAreaInterna().getNombre())
                        .build())
                .collect(Collectors.toList());

        return HospitalDetailResponse.builder()
                .codigo(hospital.getCodigo())
                .nombre(hospital.getNombre())
                .direccion(hospital.getDireccion())
                .telefono(hospital.getTelefono())
                .estado(hospital.getEstado())
                .codigoCiudad(hospital.getCiudad().getCodigo())
                .nombreCiudad(hospital.getCiudad().getNombre())
                .areasInternas(areas)
                .build();
    }

    private Ciudad validarCiudad(String codigoCiudad){
        return ciudadRepository.findById(codigoCiudad)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ciudad no encontrada con código: " + codigoCiudad));
    }
}
