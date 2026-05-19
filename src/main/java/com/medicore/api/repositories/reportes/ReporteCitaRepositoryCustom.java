package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.ProductividadMedicoDTO;
import java.util.List;

public interface ReporteCitaRepositoryCustom {
    List<ProductividadMedicoDTO> obtenerProductividadMedicaPorMes(int anio, int mes);
}