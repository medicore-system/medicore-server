package com.medicore.api.services;

import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import com.medicore.api.dtos.reportes.EstadoCarteraDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.dtos.reportes.ProductividadMedicoDTO;

import java.util.List;

public interface IReporteService {
    List<IngresosHospitalDTO> obtenerIngresosPorHospital(int anio);

    List<IngresosEspecialidadDTO> obtenerIngresosPorEspecialidad(int anio);

    List<AtencionesEpsDTO> obtenerAtencionesPorEps(int anio);

    List<ProductividadMedicoDTO> obtenerProductividadMedica(int anio, int mes);

    List<EstadoCarteraDTO> obtenerEstadoCartera();
}