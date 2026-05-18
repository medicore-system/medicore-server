package com.medicore.api.services.impl;

import com.medicore.api.dtos.ciudad.CiudadRequestDTO;
import com.medicore.api.dtos.ciudad.CiudadResponseDTO;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Departamento;
import com.medicore.api.exceptions.CiudadDuplicadaException;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.repositories.ICiudadRepository;
import com.medicore.api.repositories.DepartamentoRepository;
import com.medicore.api.services.ICiudadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CiudadServiceImpl implements ICiudadService {

    private final ICiudadRepository ciudadRepository;
    private final DepartamentoRepository departamentoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CiudadResponseDTO> listarCiudades() {
        return ciudadRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ciudad> findById(String codigo) {
        return ciudadRepository.findById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CiudadResponseDTO> buscarCiudadesPorNombre(String nombre) {
        return ciudadRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CiudadResponseDTO obtenerCiudad(String codigo) {
        Ciudad ciudad = ciudadRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Ciudad no encontrada con código: " + codigo));
        return toResponse(ciudad);
    }

    @Override
    @Transactional
    public CiudadResponseDTO crearCiudad(CiudadRequestDTO request) {
        Departamento departamento = resolverDepartamento(request.getDepartment());

        if (ciudadRepository.existsByNombreIgnoreCaseAndDepartamentoId(
                request.getName(), departamento.getId())) {
            throw new CiudadDuplicadaException(request.getName(), departamento.getNombre());
        }

        Ciudad ciudad = Ciudad.builder()
                .codigo(generarCodigo())
                .nombre(request.getName())
                .departamento(departamento)
                .estado("ACTIVE".equalsIgnoreCase(request.getStatus()))
                .build();

        return toResponse(ciudadRepository.save(ciudad));
    }

    @Override
    @Transactional
    public CiudadResponseDTO editarCiudad(String codigo, CiudadRequestDTO request) {
        Ciudad ciudad = ciudadRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Ciudad no encontrada con código: " + codigo));

        Departamento departamento = resolverDepartamento(request.getDepartment());

        boolean cambiaDatosUnicos = !ciudad.getNombre().equalsIgnoreCase(request.getName())
                || !ciudad.getDepartamento().getId().equals(departamento.getId());

        if (cambiaDatosUnicos && ciudadRepository.existsByNombreIgnoreCaseAndDepartamentoIdAndCodigoNot(
                request.getName(), departamento.getId(), codigo)) {
            throw new CiudadDuplicadaException(request.getName(), departamento.getNombre());
        }

        ciudad.setNombre(request.getName());
        ciudad.setDepartamento(departamento);
        ciudad.setEstado("ACTIVE".equalsIgnoreCase(request.getStatus()));

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

    private Departamento resolverDepartamento(String nombreDepartamento) {
        return departamentoRepository.findByNombreIgnoreCase(nombreDepartamento)
                .orElseGet(() -> departamentoRepository.save(
                        Departamento.builder()
                                .nombre(nombreDepartamento)
                                .build()));
    }

    private String generarCodigo() {
        int siguiente = ciudadRepository.findMaxCodigoSequence() + 1;
        return String.format("CIU-%03d", siguiente);
    }

    private CiudadResponseDTO toResponse(Ciudad ciudad) {
        CiudadResponseDTO response = new CiudadResponseDTO();
        response.setCode(ciudad.getCodigo());
        response.setName(ciudad.getNombre());
        response.setDepartment(ciudad.getDepartamento().getNombre());
        response.setStatus(Boolean.TRUE.equals(ciudad.getEstado()) ? "ACTIVE" : "INACTIVE");
        return response;
    }
}
