package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import java.util.List;

public interface ReporteFacturaRepositoryCustom {
    List<IngresosHospitalDTO> obtenerIngresosPorHospitalYAnio(int anio);

    List<IngresosEspecialidadDTO> obtenerIngresosPorEspecialidadYAnio(int anio);

    List<AtencionesEpsDTO> obtenerAtencionesPorEpsYAnio(int anio);
}