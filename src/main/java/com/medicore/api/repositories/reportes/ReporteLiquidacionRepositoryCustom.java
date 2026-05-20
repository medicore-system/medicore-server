package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.EstadoCarteraDTO;
import java.util.List;

/**
 * Interfaz de repositorio personalizado para reportes de cartera basados en liquidaciones.
 */
public interface ReporteLiquidacionRepositoryCustom {

    /**
     * Obtiene el estado de cartera pendiente agrupado por EPS.
     *
     * @return lista con el total pendiente de cada EPS
     */
    List<EstadoCarteraDTO> obtenerEstadoCarteraPorEps();
}