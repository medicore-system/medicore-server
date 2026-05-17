package com.medicore.api.services.impl;

import com.medicore.api.dtos.servicio.ServicioDetalleResponse;
import com.medicore.api.dtos.servicio.ServicioFacturaResponse;
import com.medicore.api.dtos.servicio.ServicioHistorialResponse;
import com.medicore.api.dtos.servicio.ServicioRequest;
import com.medicore.api.dtos.servicio.ServicioResponse;
import com.medicore.api.dtos.servicio.TipoServicioResponse;
import com.medicore.api.entities.HistorialClinico;
import com.medicore.api.entities.Servicio;
import com.medicore.api.entities.TipoServicio;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.exceptions.ServicioDuplicadoException;
import com.medicore.api.mappers.ServicioMapper;
import com.medicore.api.repositories.IFacturaRepository;
import com.medicore.api.repositories.HistorialClinicoRepository;
import com.medicore.api.repositories.IServicioRepository;
import com.medicore.api.repositories.ITipoServicioRepository;
import com.medicore.api.services.IServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements IServicioService {

    private static final int LONGITUD_MINIMA_CONSECUTIVO = 3;

    private final IServicioRepository servicioRepository;
    private final ITipoServicioRepository ITipoServicioRepository;
    private final HistorialClinicoRepository historialClinicoRepository;
    private final IFacturaRepository facturaRepository;
    private final ServicioMapper servicioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> listarServicios() {
        return servicioRepository.findAllByOrderByCodigoAsc()
                .stream()
                .map(servicioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> buscarServicios(String termino) {
        String terminoLimpio = limpiarTextoOpcional(termino);

        if (terminoLimpio == null) {
            return listarServicios();
        }

        return servicioRepository
                .findByCodigoContainingIgnoreCaseOrNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrderByCodigoAsc(
                        terminoLimpio,
                        terminoLimpio,
                        terminoLimpio
                )
                .stream()
                .map(servicioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioDetalleResponse obtenerDetalleServicio(String codigo) {
        Servicio servicio = obtenerServicio(codigo);
        ServicioHistorialResponse historial = servicioMapper.toHistorialResponse(servicio.getHistorialClinico());
        List<ServicioFacturaResponse> facturas = obtenerFacturasDelServicio(codigo);

        return servicioMapper.toDetalleResponse(servicio, historial, facturas);
    }

    @Override
    @Transactional
    public ServicioResponse crearServicio(ServicioRequest request) {
        validarNombreDisponible(request.getNombre());

        TipoServicio tipoServicio = obtenerTipoServicio(request.getIdTipoServicio());
        HistorialClinico historialClinico = obtenerHistorialOpcional(request.getCodigoHistorial());
        Servicio servicio = construirServicio(request, tipoServicio, historialClinico);

        return servicioMapper.toResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public ServicioResponse editarServicio(String codigo, ServicioRequest request) {
        Servicio servicio = obtenerServicio(codigo);
        validarNombreDisponibleParaEdicion(request.getNombre(), codigo);

        TipoServicio tipoServicio = obtenerTipoServicio(request.getIdTipoServicio());
        HistorialClinico historialClinico = obtenerHistorialOpcional(request.getCodigoHistorial());
        actualizarServicio(servicio, request, tipoServicio, historialClinico);

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
        return ITipoServicioRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(servicioMapper::toTipoServicioResponse)
                .toList();
    }

    private List<ServicioFacturaResponse> obtenerFacturasDelServicio(String codigoServicio) {
        return facturaRepository.findByServicioCodigoOrderByFechaDescCodigoAsc(codigoServicio)
                .stream()
                .map(servicioMapper::toFacturaResponse)
                .toList();
    }

    private Servicio construirServicio(
            ServicioRequest request,
            TipoServicio tipoServicio,
            HistorialClinico historialClinico
    ) {
        return Servicio.builder()
                .codigo(generarCodigo(tipoServicio))
                .nombre(limpiarTextoObligatorio(request.getNombre()))
                .descripcion(limpiarTextoObligatorio(request.getDescripcion()))
                .tipoServicio(tipoServicio)
                .costo(request.getPrecio())
                .procedimiento(limpiarTextoOpcional(request.getProcedimiento()))
                .resultados(limpiarTextoOpcional(request.getResultados()))
                .historialClinico(historialClinico)
                .estado(estadoInicial(request.getEstado()))
                .build();
    }

    private void actualizarServicio(
            Servicio servicio,
            ServicioRequest request,
            TipoServicio tipoServicio,
            HistorialClinico historialClinico
    ) {
        servicio.setNombre(limpiarTextoObligatorio(request.getNombre()));
        servicio.setDescripcion(limpiarTextoObligatorio(request.getDescripcion()));
        servicio.setTipoServicio(tipoServicio);
        servicio.setCosto(request.getPrecio());
        servicio.setProcedimiento(limpiarTextoOpcional(request.getProcedimiento()));
        servicio.setResultados(limpiarTextoOpcional(request.getResultados()));
        servicio.setHistorialClinico(historialClinico);

        if (request.getEstado() != null) {
            servicio.setEstado(request.getEstado());
        }
    }

    private Servicio obtenerServicio(String codigo) {
        return servicioRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Servicio no encontrado con código: " + codigo
                ));
    }

    private TipoServicio obtenerTipoServicio(Integer idTipoServicio) {
        return ITipoServicioRepository.findById(idTipoServicio)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Tipo de servicio no encontrado con id: " + idTipoServicio
                ));
    }

    private HistorialClinico obtenerHistorialOpcional(String codigoHistorial) {
        String codigoLimpio = limpiarTextoOpcional(codigoHistorial);

        if (codigoLimpio == null) {
            return null;
        }

        return historialClinicoRepository.findById(codigoLimpio)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Historial clínico no encontrado con código: " + codigoLimpio
                ));
    }

    private void validarNombreDisponible(String nombre) {
        String nombreLimpio = limpiarTextoObligatorio(nombre);

        if (servicioRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new ServicioDuplicadoException(nombreLimpio);
        }
    }

    private void validarNombreDisponibleParaEdicion(String nombre, String codigo) {
        String nombreLimpio = limpiarTextoObligatorio(nombre);

        if (servicioRepository.existsByNombreIgnoreCaseAndCodigoNot(nombreLimpio, codigo)) {
            throw new ServicioDuplicadoException(nombreLimpio);
        }
    }

    private String generarCodigo(TipoServicio tipoServicio) {
        String prefijoConGuion = tipoServicio.getPrefijo() + "-";
        int siguiente = obtenerMayorConsecutivo(prefijoConGuion) + 1;
        String formato = "%s-%0" + LONGITUD_MINIMA_CONSECUTIVO + "d";

        return String.format(formato, tipoServicio.getPrefijo(), siguiente);
    }

    private int obtenerMayorConsecutivo(String prefijoConGuion) {
        return servicioRepository.findByCodigoStartingWith(prefijoConGuion)
                .stream()
                .map(Servicio::getCodigo)
                .filter(codigo -> tieneConsecutivoNumerico(codigo, prefijoConGuion))
                .mapToInt(codigo -> extraerConsecutivo(codigo, prefijoConGuion))
                .max()
                .orElse(0);
    }

    private boolean tieneConsecutivoNumerico(String codigo, String prefijoConGuion) {
        if (codigo == null || !codigo.startsWith(prefijoConGuion)) {
            return false;
        }

        String consecutivo = codigo.substring(prefijoConGuion.length());
        return !consecutivo.isBlank() && consecutivo.chars().allMatch(Character::isDigit);
    }

    private int extraerConsecutivo(String codigo, String prefijoConGuion) {
        return Integer.parseInt(codigo.substring(prefijoConGuion.length()));
    }

    private Boolean estadoInicial(Boolean estado) {
        return estado == null ? Boolean.TRUE : estado;
    }

    private String limpiarTextoObligatorio(String valor) {
        return valor.trim();
    }

    private String limpiarTextoOpcional(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }

        return valor.trim();
    }
}