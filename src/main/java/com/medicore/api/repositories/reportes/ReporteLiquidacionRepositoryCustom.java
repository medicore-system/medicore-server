package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.EstadoCarteraDTO;
import java.util.List;

public interface ReporteLiquidacionRepositoryCustom {
    List<EstadoCarteraDTO> obtenerEstadoCarteraPorEps();
}