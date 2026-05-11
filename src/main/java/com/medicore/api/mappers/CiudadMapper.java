package com.medicore.api.mappers;

import com.medicore.api.dtos.servicio.*;
import com.medicore.api.entities.Servicio;
import com.medicore.api.entities.TipoServicio;
import com.medicore.api.repositories.ServicioRepository;
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
                .codigoHistorial(servicio.getCodigoHistorial())
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
                .codigoHistorial(servicio.getCodigoHistorial())
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

    public ServicioHistorialResponse toHistorialResponse(
            ServicioRepository.HistorialClinicoResumenProjection projection
    ) {
        if (projection == null) {
            return null;
        }

        return ServicioHistorialResponse.builder()
                .codigo(projection.getCodigo())
                .fecha(projection.getFecha())
                .tipo(projection.getTipo())
                .descripcion(projection.getDescripcion())
                .documentoPaciente(projection.getDocumentoPaciente())
                .nombrePaciente(projection.getNombrePaciente())
                .documentoMedico(projection.getDocumentoMedico())
                .nombreMedico(projection.getNombreMedico())
                .build();
    }

    public ServicioFacturaResponse toFacturaResponse(
            ServicioRepository.FacturaServicioProjection projection
    ) {
        return ServicioFacturaResponse.builder()
                .codigo(projection.getCodigo())
                .fecha(projection.getFecha())
                .estado(projection.getEstado())
                .costoTotal(projection.getCostoTotal())
                .descripcion(projection.getDescripcion())
                .codigoCita(projection.getCodigoCita())
                .codigoEps(projection.getCodigoEps())
                .nombreEps(projection.getNombreEps())
                .codigoHospital(projection.getCodigoHospital())
                .nombreHospital(projection.getNombreHospital())
                .build();
    }
}