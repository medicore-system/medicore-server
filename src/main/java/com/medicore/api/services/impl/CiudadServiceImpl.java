package com.medicore.api.services.impl;

import com.medicore.api.dtos.CiudadRequest;
import com.medicore.api.dtos.CiudadResponse;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Departamento;
import com.medicore.api.entities.Medico;
import com.medicore.api.exceptions.CiudadDuplicadaException;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.mappers.CiudadMapper;
import com.medicore.api.repositories.CiudadRepository;
import com.medicore.api.repositories.DepartamentoRepository;
import com.medicore.api.repositories.hospital.HospitalRepository;
import com.medicore.api.services.ICiudadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CiudadServiceImpl implements ICiudadService {

    private final CiudadRepository ciudadRepository;
    private final DepartamentoRepository departamentoRepository;
    private final HospitalRepository hospitalRepository;
    private final CiudadMapper ciudadMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CiudadResponse> listarCiudades() {
        return ciudadRepository.findByEstadoTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Optional<Ciudad> findById(String codigo) {
        return ciudadRepository.findById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CiudadResponse> buscarCiudadesPorNombre(String nombre) {
        return ciudadRepository.findByNombreContainingIgnoreCaseAndEstadoTrue(nombre).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CiudadResponse obtenerCiudad(String codigo) {
        Ciudad ciudad = ciudadRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Ciudad no encontrada con código: " + codigo));
        return toResponse(ciudad);
    }

    @Override
    @Transactional
    public CiudadResponse crearCiudad(CiudadRequest request) {
        Departamento departamento = obtenerDepartamento(request.getIdDepartamento());

        if (ciudadRepository.existsByNombreIgnoreCaseAndDepartamentoId(
                request.getNombre(), request.getIdDepartamento())) {
            throw new CiudadDuplicadaException(request.getNombre(), departamento.getNombre());
        }

        Ciudad ciudad = Ciudad.builder()
                .codigo(generarCodigo())
                .nombre(request.getNombre())
                .departamento(departamento)
                .estado(true)
                .build();

        return toResponse(ciudadRepository.save(ciudad));
    }

    @Override
    @Transactional
    public CiudadResponse editarCiudad(String codigo, CiudadRequest request) {
        Ciudad ciudad = ciudadRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Ciudad no encontrada con código: " + codigo));

        Departamento departamento = obtenerDepartamento(request.getIdDepartamento());

        boolean cambiaDatosUnicos = !ciudad.getNombre().equalsIgnoreCase(request.getNombre())
                || !ciudad.getDepartamento().getId().equals(request.getIdDepartamento());

        if (cambiaDatosUnicos && ciudadRepository.existsByNombreIgnoreCaseAndDepartamentoIdAndCodigoNot(
                request.getNombre(), request.getIdDepartamento(), codigo)) {
            throw new CiudadDuplicadaException(request.getNombre(), departamento.getNombre());
        }

        ciudad.setNombre(request.getNombre());
        ciudad.setDepartamento(departamento);

        return toResponse(ciudadRepository.save(ciudad));
    }

    @Override
    @Transactional
    public void eliminarCiudad(String codigo) {
        Ciudad ciudad = ciudadRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Ciudad no encontrada con código: " + codigo));
        ciudad.setEstado(false);
        ciudadRepository.save(ciudad);
    }

    private Departamento obtenerDepartamento(Integer idDepartamento) {
        return departamentoRepository.findById(idDepartamento)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Departamento no encontrado con id: " + idDepartamento));
    }

    private String generarCodigo() {
        int siguiente = ciudadRepository.findMaxCodigoSequence() + 1;
        return String.format("COL-%03d", siguiente);
    }

    private CiudadResponse toResponse(Ciudad ciudad) {
        Long totalHospitales = hospitalRepository.countByCiudadCodigo(ciudad.getCodigo());
        return ciudadMapper.toResponse(ciudad, totalHospitales);
    }
}
