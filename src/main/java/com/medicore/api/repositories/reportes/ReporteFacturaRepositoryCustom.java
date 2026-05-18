package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import java.util.List;

public interface ReporteFacturaRepositoryCustom {
    List<IngresosHospitalDTO> obtenerIngresosPorHospitalYAnio(int anio);
}