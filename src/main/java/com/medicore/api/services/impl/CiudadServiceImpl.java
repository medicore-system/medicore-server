package com.medicore.api.services.impl;

import com.medicore.api.dtos.ciudad.CiudadRequestDTO;
import com.medicore.api.dtos.ciudad.CiudadResponseDTO;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Departamento;
import com.medicore.api.exceptions.CiudadDuplicadaException;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.repositories.ICiudadRepository;
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

    private final ICiudadRepository ciudadRepository;
    private final DepartamentoRepository departamentoRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CiudadResponseDTO> listarCiudades() {
        return ciudadRepository.findByEstadoTrue().stream()
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
        return ciudadRepository.findByNombreContainingIgnoreCaseAndEstadoTrue(nombre).stream()
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
    public CiudadResponseDTO editarCiudad(String codigo, CiudadRequestDTO request) {
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

    private CiudadResponseDTO toResponse(Ciudad ciudad) {
        Long totalHospitales = hospitalRepository.countByCiudadCodigo(ciudad.getCodigo());
        CiudadResponseDTO response = new CiudadResponseDTO();
        response.setCodigo(ciudad.getCodigo());
        response.setNombre(ciudad.getNombre());
        response.setDepartamento(ciudad.getDepartamento().getNombre());
        response.setTotalHospitales(totalHospitales);
        return response;
    }
}
