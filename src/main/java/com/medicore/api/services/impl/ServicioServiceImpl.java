package com.medicore.api.services.impl;

import com.medicore.api.dtos.servicio.*;
import com.medicore.api.entities.Servicio;
import com.medicore.api.entities.TipoServicio;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.exceptions.ServicioDuplicadoException;
import com.medicore.api.mappers.ServicioMapper;
import com.medicore.api.repositories.ServicioRepository;
import com.medicore.api.repositories.TipoServicioRepository;
import com.medicore.api.services.IServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements IServicioService {

    private final ServicioRepository servicioRepository;
    private final TipoServicioRepository tipoServicioRepository;
    private final ServicioMapper servicioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> listarServicios() {
        return servicioRepository.findAllByOrderByCodigoAsc().stream()
                .map(servicioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> buscarServicios(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return listarServicios();
        }

        return servicioRepository.buscarPorTermino(termino.trim()).stream()
                .map(servicioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioDetalleResponse obtenerDetalleServicio(String codigo) {
        Servicio servicio = obtenerServicio(codigo);

        ServicioHistorialResponse historial = servicioRepository.findHistorialByServicioCodigo(codigo)
                .map(servicioMapper::toHistorialResponse)
                .orElse(null);

        List<ServicioFacturaResponse> facturas = servicioRepository.findFacturasByServicioCodigo(codigo)
                .stream()
                .map(servicioMapper::toFacturaResponse)
                .toList();

        return servicioMapper.toDetalleResponse(servicio, historial, facturas);
    }

    @Override
    @Transactional
    public ServicioResponse crearServicio(ServicioRequest request) {
        validarNombreDuplicado(request.getNombre());
        validarHistorialSiViene(request.getCodigoHistorial());

        TipoServicio tipoServicio = obtenerTipoServicio(request.getIdTipoServicio());

        Servicio servicio = Servicio.builder()
                .codigo(generarCodigo(tipoServicio))
                .nombre(request.getNombre().trim())
                .descripcion(request.getDescripcion().trim())
                .tipoServicio(tipoServicio)
                .costo(request.getPrecio())
                .procedimiento(limpiarTextoOpcional(request.getProcedimiento()))
                .resultados(limpiarTextoOpcional(request.getResultados()))
                .codigoHistorial(limpiarTextoOpcional(request.getCodigoHistorial()))
                .estado(request.getEstado() == null ? true : request.getEstado())
                .build();

        return servicioMapper.toResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public ServicioResponse editarServicio(String codigo, ServicioRequest request) {
        Servicio servicio = obtenerServicio(codigo);
        validarNombreDuplicadoEnOtroServicio(request.getNombre(), codigo);
        validarHistorialSiViene(request.getCodigoHistorial());

        TipoServicio tipoServicio = obtenerTipoServicio(request.getIdTipoServicio());

        servicio.setNombre(request.getNombre().trim());
        servicio.setDescripcion(request.getDescripcion().trim());
        servicio.setTipoServicio(tipoServicio);
        servicio.setCosto(request.getPrecio());
        servicio.setProcedimiento(limpiarTextoOpcional(request.getProcedimiento()));
        servicio.setResultados(limpiarTextoOpcional(request.getResultados()));
        servicio.setCodigoHistorial(limpiarTextoOpcional(request.getCodigoHistorial()));

        if (request.getEstado() != null) {
            servicio.setEstado(request.getEstado());
        }

        return servicioMapper.toResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public void inactivarServicio(String codigo) {
        Servicio servicio = obtenerServicio(codigo);
        servicio.setEstado(false);
        servicioRepository.save(servicio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoServicioResponse> listarTiposServicio() {
        return tipoServicioRepository.findAllByOrderByNombreAsc().stream()
                .map(servicioMapper::toTipoServicioResponse)
                .toList();
    }

    private Servicio obtenerServicio(String codigo) {
        return servicioRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Servicio no encontrado con código: " + codigo
                ));
    }

    private TipoServicio obtenerTipoServicio(Integer idTipoServicio) {
        return tipoServicioRepository.findById(idTipoServicio)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Tipo de servicio no encontrado con id: " + idTipoServicio
                ));
    }

    private void validarNombreDuplicado(String nombre) {
        if (servicioRepository.existsByNombreIgnoreCase(nombre.trim())) {
            throw new ServicioDuplicadoException(nombre);
        }
    }

    private void validarNombreDuplicadoEnOtroServicio(String nombre, String codigo) {
        if (servicioRepository.existsByNombreIgnoreCaseAndCodigoNot(nombre.trim(), codigo)) {
            throw new ServicioDuplicadoException(nombre);
        }
    }

    private void validarHistorialSiViene(String codigoHistorial) {
        String codigoLimpio = limpiarTextoOpcional(codigoHistorial);

        if (codigoLimpio == null) {
            return;
        }

        if (!servicioRepository.existsHistorialClinicoByCodigo(codigoLimpio)) {
            throw new RecursoNoEncontradoException(
                    "Historial clínico no encontrado con código: " + codigoLimpio
            );
        }
    }

    private String generarCodigo(TipoServicio tipoServicio) {
        String prefijo = tipoServicio.getPrefijo();
        int siguiente = servicioRepository.findMaxCodigoSequenceByPrefijo(prefijo) + 1;
        return String.format("%s-%03d", prefijo, siguiente);
    }

    private String limpiarTextoOpcional(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }
}