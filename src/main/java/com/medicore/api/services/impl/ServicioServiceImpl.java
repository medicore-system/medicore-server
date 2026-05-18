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
import com.medicore.api.repositories.IHistorialClinicoRepository;
import com.medicore.api.repositories.IServicioRepository;
import com.medicore.api.repositories.ITipoServicioRepository;
import com.medicore.api.services.IServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de la lógica de negocio para la gestión de servicios médicos.
 *
 * <p>
 * Esta clase coordina la creación, consulta, edición e inactivación de servicios.
 * También se encarga de validar reglas de negocio como nombres duplicados,
 * existencia de tipos de servicio y asociación opcional con historiales clínicos.
 * </p>
 *
 * <p>
 * La clase evita consultas SQL quemadas delegando el acceso a datos en repositorios
 * basados en métodos derivados de Spring Data JPA.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements IServicioService {

    /**
     * Cantidad mínima de dígitos usada para generar códigos consecutivos.
     *
     * <p>
     * Ejemplo: {@code MED-001}, {@code EXA-002}, {@code PROC-003}.
     * </p>
     */
    private static final int LONGITUD_MINIMA_CONSECUTIVO = 3;

    private final IServicioRepository servicioRepository;
    private final ITipoServicioRepository tipoServicioRepository;
    private final IHistorialClinicoRepository historialClinicoRepository;
    private final IFacturaRepository facturaRepository;
    private final ServicioMapper servicioMapper;

    /**
     * Lista todos los servicios médicos registrados.
     *
     * @return lista de servicios convertidos a DTO de respuesta.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> listarServicios() {
        return servicioRepository.findAllByOrderByCodigoAsc()
                .stream()
                .map(servicioMapper::toResponse)
                .toList();
    }

    /**
     * Busca servicios médicos por un término de búsqueda.
     *
     * <p>
     * Si el término recibido es nulo o vacío, se listan todos los servicios.
     * Si contiene texto, se busca por código, nombre o descripción.
     * </p>
     *
     * @param termino texto usado como criterio de búsqueda.
     * @return lista de servicios que coinciden con el término indicado.
     */
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

    /**
     * Obtiene el detalle completo de un servicio médico.
     *
     * <p>
     * El detalle incluye la información básica del servicio, el historial clínico asociado
     * si existe y las facturas relacionadas con el servicio.
     * </p>
     *
     * @param codigo código único del servicio.
     * @return detalle completo del servicio.
     * @throws RecursoNoEncontradoException si el servicio no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public ServicioDetalleResponse obtenerDetalleServicio(String codigo) {
        Servicio servicio = obtenerServicio(codigo);
        ServicioHistorialResponse historial = servicioMapper.toHistorialResponse(servicio.getHistorialClinico());
        List<ServicioFacturaResponse> facturas = obtenerFacturasDelServicio(codigo);

        return servicioMapper.toDetalleResponse(servicio, historial, facturas);
    }

    /**
     * Crea un nuevo servicio médico.
     *
     * <p>
     * Antes de guardar, valida que el nombre no esté duplicado, obtiene el tipo
     * de servicio, valida el historial clínico si fue enviado y genera el código
     * automático del servicio.
     * </p>
     *
     * @param request datos necesarios para crear el servicio.
     * @return servicio creado.
     * @throws ServicioDuplicadoException si ya existe un servicio con el mismo nombre.
     * @throws RecursoNoEncontradoException si el tipo de servicio o historial clínico no existe.
     */
    @Override
    @Transactional
    public ServicioResponse crearServicio(ServicioRequest request) {
        validarNombreDisponible(request.getNombre());

        TipoServicio tipoServicio = obtenerTipoServicio(request.getIdTipoServicio());
        HistorialClinico historialClinico = obtenerHistorialOpcional(request.getCodigoHistorial());
        Servicio servicio = construirServicio(request, tipoServicio, historialClinico);

        return servicioMapper.toResponse(servicioRepository.save(servicio));
    }

    /**
     * Edita un servicio médico existente.
     *
     * <p>
     * El servicio se busca por código. Luego se validan duplicados,
     * tipo de servicio e historial clínico antes de actualizar la entidad.
     * </p>
     *
     * @param codigo código del servicio a editar.
     * @param request datos nuevos del servicio.
     * @return servicio actualizado.
     * @throws RecursoNoEncontradoException si el servicio, tipo o historial no existe.
     * @throws ServicioDuplicadoException si otro servicio ya usa el mismo nombre.
     */
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

    /**
     * Inactiva un servicio médico.
     *
     * <p>
     * Esta operación realiza una eliminación lógica, cambiando el estado del servicio
     * a {@code false}. El registro permanece en la base de datos.
     * </p>
     *
     * @param codigo código del servicio a inactivar.
     * @throws RecursoNoEncontradoException si el servicio no existe.
     */
    @Override
    @Transactional
    public void inactivarServicio(String codigo) {
        Servicio servicio = obtenerServicio(codigo);
        servicio.setEstado(false);
        servicioRepository.save(servicio);
    }

    /**
     * Lista los tipos de servicio disponibles en el sistema.
     *
     * @return lista de tipos de servicio convertidos a DTO.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TipoServicioResponse> listarTiposServicio() {
        return tipoServicioRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(servicioMapper::toTipoServicioResponse)
                .toList();
    }

    /**
     * Obtiene las facturas asociadas a un servicio médico.
     *
     * @param codigoServicio código del servicio.
     * @return lista de facturas asociadas al servicio.
     */
    private List<ServicioFacturaResponse> obtenerFacturasDelServicio(String codigoServicio) {
        return facturaRepository.findByServicioCodigoOrderByFechaDescCodigoAsc(codigoServicio)
                .stream()
                .map(servicioMapper::toFacturaResponse)
                .toList();
    }

    /**
     * Construye una entidad {@link Servicio} a partir del request recibido.
     *
     * @param request datos enviados por el cliente.
     * @param tipoServicio tipo de servicio ya validado.
     * @param historialClinico historial clínico asociado, si existe.
     * @return entidad Servicio lista para persistir.
     */
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

    /**
     * Actualiza los campos modificables de un servicio existente.
     *
     * @param servicio entidad existente.
     * @param request datos nuevos.
     * @param tipoServicio tipo de servicio validado.
     * @param historialClinico historial clínico validado o nulo.
     */
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

    /**
     * Busca un servicio por su código.
     *
     * @param codigo código del servicio.
     * @return entidad Servicio encontrada.
     * @throws RecursoNoEncontradoException si no existe un servicio con el código indicado.
     */
    private Servicio obtenerServicio(String codigo) {
        return servicioRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Servicio no encontrado con código: " + codigo
                ));
    }

    /**
     * Busca un tipo de servicio por su identificador.
     *
     * @param idTipoServicio identificador del tipo de servicio.
     * @return entidad TipoServicio encontrada.
     * @throws RecursoNoEncontradoException si no existe el tipo de servicio.
     */
    private TipoServicio obtenerTipoServicio(Integer idTipoServicio) {
        return tipoServicioRepository.findById(idTipoServicio)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Tipo de servicio no encontrado con id: " + idTipoServicio
                ));
    }

    /**
     * Obtiene el historial clínico asociado si el código fue enviado.
     *
     * @param codigoHistorial código del historial clínico.
     * @return historial clínico encontrado o {@code null} si no fue enviado.
     * @throws RecursoNoEncontradoException si el código fue enviado pero no existe.
     */
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

    /**
     * Valida que el nombre del servicio no esté registrado.
     *
     * @param nombre nombre a validar.
     * @throws ServicioDuplicadoException si el nombre ya existe.
     */
    private void validarNombreDisponible(String nombre) {
        String nombreLimpio = limpiarTextoObligatorio(nombre);

        if (servicioRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new ServicioDuplicadoException(nombreLimpio);
        }
    }

    /**
     * Valida que el nombre indicado no pertenezca a otro servicio.
     *
     * @param nombre nombre a validar.
     * @param codigo código del servicio actual.
     * @throws ServicioDuplicadoException si otro servicio ya usa el nombre.
     */
    private void validarNombreDisponibleParaEdicion(String nombre, String codigo) {
        String nombreLimpio = limpiarTextoObligatorio(nombre);

        if (servicioRepository.existsByNombreIgnoreCaseAndCodigoNot(nombreLimpio, codigo)) {
            throw new ServicioDuplicadoException(nombreLimpio);
        }
    }

    /**
     * Genera el código automático de un servicio a partir del prefijo del tipo.
     *
     * @param tipoServicio tipo de servicio usado para obtener el prefijo.
     * @return código generado, por ejemplo {@code MED-001}.
     */
    private String generarCodigo(TipoServicio tipoServicio) {
        String prefijoConGuion = tipoServicio.getPrefijo() + "-";
        int siguiente = obtenerMayorConsecutivo(prefijoConGuion) + 1;
        String formato = "%s-%0" + LONGITUD_MINIMA_CONSECUTIVO + "d";

        return String.format(formato, tipoServicio.getPrefijo(), siguiente);
    }

    /**
     * Obtiene el mayor consecutivo numérico existente para un prefijo.
     *
     * @param prefijoConGuion prefijo del código, por ejemplo {@code MED-}.
     * @return mayor consecutivo encontrado o cero si no existen registros.
     */
    private int obtenerMayorConsecutivo(String prefijoConGuion) {
        return servicioRepository.findByCodigoStartingWith(prefijoConGuion)
                .stream()
                .map(Servicio::getCodigo)
                .filter(codigo -> tieneConsecutivoNumerico(codigo, prefijoConGuion))
                .mapToInt(codigo -> extraerConsecutivo(codigo, prefijoConGuion))
                .max()
                .orElse(0);
    }

    /**
     * Valida si un código contiene un consecutivo numérico después del prefijo.
     *
     * @param codigo código completo del servicio.
     * @param prefijoConGuion prefijo esperado.
     * @return {@code true} si el código contiene un consecutivo numérico válido.
     */
    private boolean tieneConsecutivoNumerico(String codigo, String prefijoConGuion) {
        if (codigo == null || !codigo.startsWith(prefijoConGuion)) {
            return false;
        }

        String consecutivo = codigo.substring(prefijoConGuion.length());
        return !consecutivo.isBlank() && consecutivo.chars().allMatch(Character::isDigit);
    }

    /**
     * Extrae el número consecutivo de un código de servicio.
     *
     * @param codigo código completo.
     * @param prefijoConGuion prefijo que debe removerse.
     * @return consecutivo numérico.
     */
    private int extraerConsecutivo(String codigo, String prefijoConGuion) {
        return Integer.parseInt(codigo.substring(prefijoConGuion.length()));
    }

    /**
     * Define el estado inicial de un servicio.
     *
     * @param estado estado enviado en la petición.
     * @return {@code true} si no se envió estado; de lo contrario, el valor recibido.
     */
    private Boolean estadoInicial(Boolean estado) {
        return estado == null ? Boolean.TRUE : estado;
    }

    /**
     * Limpia espacios en blanco de un texto obligatorio.
     *
     * @param valor texto obligatorio.
     * @return texto sin espacios laterales.
     */
    private String limpiarTextoObligatorio(String valor) {
        return valor.trim();
    }

    /**
     * Limpia un texto opcional.
     *
     * @param valor texto opcional.
     * @return texto sin espacios laterales o {@code null} si está vacío.
     */
    private String limpiarTextoOpcional(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }

        return valor.trim();
    }
}