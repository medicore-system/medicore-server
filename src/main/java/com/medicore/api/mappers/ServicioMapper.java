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

@Component
public class ServicioMapper {

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

    public TipoServicioResponse toTipoServicioResponse(TipoServicio tipoServicio) {
        return TipoServicioResponse.builder()
                .id(tipoServicio.getId())
                .nombre(tipoServicio.getNombre())
                .prefijo(tipoServicio.getPrefijo())
                .build();
    }

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

    private String obtenerCodigoHistorial(Servicio servicio) {
        return servicio.getHistorialClinico() != null
                ? servicio.getHistorialClinico().getCodigo()
                : null;
    }

    private String nombreCompleto(String nombre, String apellido) {
        return String.join(" ", valorSeguro(nombre), valorSeguro(apellido)).trim();
    }

    private String valorSeguro(String valor) {
        return valor == null ? "" : valor;
    }
}