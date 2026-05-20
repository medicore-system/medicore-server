package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.ProductividadMedicoDTO;
import java.util.List;

/**
 * Interfaz de repositorio personalizado para reportes basados en citas médicas.
 */
public interface ReporteCitaRepositoryCustom {

    /**
     * Calcula la productividad de cada médico (número de citas completadas) para un mes y año dados.
     *
     * @param anio año del reporte
     * @param mes  mes del reporte
     * @return lista de DTOs con la productividad de cada médico
     */
    List<ProductividadMedicoDTO> obtenerProductividadMedicaPorMes(int anio, int mes);
}