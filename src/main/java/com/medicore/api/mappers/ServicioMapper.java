package com.medicore.api.mappers;

import com.medicore.api.dtos.servicio.ServicioDetalleResponse;
import com.medicore.api.dtos.servicio.ServicioFacturaResponse;
import com.medicore.api.dtos.servicio.ServicioHistorialResponse;
import com.medicore.api.dtos.servicio.ServicioResponse;
import com.medicore.api.dtos.servicio.TipoServicioResponse;
import com.medicore.api.entities.Factura;
import com.medicore.api.entities.HistorialClinico;
import com.medicore.api.entities.Medico;
import com.medicore.api.entities.Servicio;
import com.medicore.api.entities.TipoServicio;
import com.medicore.api.entities.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper encargado de convertir entidades del dominio de servicios médicos
 * en objetos DTO de respuesta.
 *
 * <p>
 * Esta clase evita que los controladores y servicios expongan directamente
 * las entidades JPA. De esta manera se mantiene una separación clara entre
 * el modelo de persistencia y el contrato de respuesta de la API.
 * </p>
 */
@Component
public class ServicioMapper {

    /**
     * Convierte una entidad {@link Servicio} en un DTO simple de respuesta.
     *
     * @param servicio entidad de servicio.
     * @return DTO con la información básica del servicio.
     */
    public ServicioResponse toResponse(Servicio servicio) {
        return ServicioResponse.builder()
                .codigo(servicio.getCodigo())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .idTipoServicio(servicio.getTipoServicio().getId())
                .tipo(servicio.getTipoServicio().getNombre())
                .precio(servicio.getCosto())
                .estado(servicio.getEstado())
                .procedimiento(servicio.getProcedimiento())
                .resultados(servicio.getResultados())
                .codigoHistorial(obtenerCodigoHistorial(servicio))
                .build();
    }

    /**
     * Construye el DTO de detalle completo de un servicio.
     *
     * @param servicio entidad principal del servicio.
     * @param historialClinico historial clínico asociado convertido a DTO.
     * @param facturas facturas asociadas convertidas a DTO.
     * @return DTO de detalle completo.
     */
    public ServicioDetalleResponse toDetalleResponse(
            Servicio servicio,
            ServicioHistorialResponse historialClinico,
            List<ServicioFacturaResponse> facturas
    ) {
        return ServicioDetalleResponse.builder()
                .codigo(servicio.getCodigo())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .idTipoServicio(servicio.getTipoServicio().getId())
                .tipo(servicio.getTipoServicio().getNombre())
                .precio(servicio.getCosto())
                .estado(servicio.getEstado())
                .procedimiento(servicio.getProcedimiento())
                .resultados(servicio.getResultados())
                .codigoHistorial(obtenerCodigoHistorial(servicio))
                .historialClinico(historialClinico)
                .facturas(facturas)
                .build();
    }

    /**
     * Convierte un tipo de servicio a DTO de respuesta.
     *
     * @param tipoServicio entidad de tipo de servicio.
     * @return DTO del tipo de servicio.
     */
    public TipoServicioResponse toTipoServicioResponse(TipoServicio tipoServicio) {
        return TipoServicioResponse.builder()
                .id(tipoServicio.getId())
                .nombre(tipoServicio.getNombre())
                .prefijo(tipoServicio.getPrefijo())
                .build();
    }

    /**
     * Convierte un historial clínico asociado al servicio en DTO.
     *
     * @param historialClinico entidad de historial clínico.
     * @return DTO del historial clínico o {@code null} si no existe.
     */
    public ServicioHistorialResponse toHistorialResponse(HistorialClinico historialClinico) {
        if (historialClinico == null) {
            return null;
        }

        Usuario paciente = historialClinico.getPaciente();
        Medico medico = historialClinico.getMedico();

        return ServicioHistorialResponse.builder()
                .codigo(historialClinico.getCodigo())
                .fecha(historialClinico.getFecha())
                .tipo(historialClinico.getTipo())
                .descripcion(historialClinico.getDescripcion())
                .documentoPaciente(paciente != null ? paciente.getDocumento() : null)
                .nombrePaciente(paciente != null ? nombreCompleto(paciente.getNombre(), paciente.getApellido()) : null)
                .documentoMedico(medico != null ? medico.getDocumento() : null)
                .nombreMedico(medico != null ? nombreCompleto(medico.getNombre(), medico.getApellido()) : null)
                .build();
    }

    /**
     * Convierte una factura relacionada con un servicio en DTO.
     *
     * @param factura entidad factura.
     * @return DTO de factura asociada al servicio.
     */
    public ServicioFacturaResponse toFacturaResponse(Factura factura) {
        return ServicioFacturaResponse.builder()
                .codigo(factura.getCodigo())
                .fecha(factura.getFecha())
                .estado(factura.getEstado())
                .costoTotal(factura.getCostoTotal())
                .descripcion(factura.getDescripcion())
                .codigoCita(factura.getCita() != null ? factura.getCita().getCodigo() : null)
                .codigoEps(factura.getEps() != null ? factura.getEps().getCodigo() : null)
                .nombreEps(factura.getEps() != null ? factura.getEps().getNombre() : null)
                .codigoHospital(factura.getHospital() != null ? factura.getHospital().getCodigo() : null)
                .nombreHospital(factura.getHospital() != null ? factura.getHospital().getNombre() : null)
                .build();
    }

    /**
     * Obtiene el código del historial clínico asociado a un servicio.
     *
     * @param servicio entidad servicio.
     * @return código del historial clínico o {@code null} si no existe.
     */
    private String obtenerCodigoHistorial(Servicio servicio) {
        return servicio.getHistorialClinico() != null
                ? servicio.getHistorialClinico().getCodigo()
                : null;
    }

    /**
     * Construye un nombre completo a partir de nombre y apellido.
     *
     * @param nombre nombre de la persona.
     * @param apellido apellido de la persona.
     * @return nombre completo sin espacios sobrantes.
     */
    private String nombreCompleto(String nombre, String apellido) {
        return String.join(" ", valorSeguro(nombre), valorSeguro(apellido)).trim();
    }

    /**
     * Convierte un valor nulo en cadena vacía.
     *
     * @param valor texto a validar.
     * @return texto original o cadena vacía si es nulo.
     */
    private String valorSeguro(String valor) {
        return valor == null ? "" : valor;
    }
}