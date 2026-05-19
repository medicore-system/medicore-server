package com.medicore.api.services;

import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import java.util.List;

public interface IReporteService {
    List<IngresosHospitalDTO> obtenerIngresosPorHospital(int anio);
}