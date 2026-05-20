package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import java.util.List;

/**
 * Interfaz de repositorio personalizado para reportes financieros basados en facturas.
 */
public interface ReporteFacturaRepositoryCustom {

    /**
     * Obtiene los ingresos totales agrupados por hospital para un año dado.
     *
     * @param anio año del reporte
     * @return lista de ingresos por hospital
     */
    List<IngresosHospitalDTO> obtenerIngresosPorHospitalYAnio(int anio);

    /**
     * Obtiene los ingresos totales agrupados por especialidad médica para un año dado.
     *
     * @param anio año del reporte
     * @return lista de ingresos por especialidad
     */
    List<IngresosEspecialidadDTO> obtenerIngresosPorEspecialidadYAnio(int anio);

    /**
     * Obtiene el número de atenciones y el monto total agrupados por EPS para un año dado.
     *
     * @param anio año del reporte
     * @return lista de atenciones por EPS
     */
    List<AtencionesEpsDTO> obtenerAtencionesPorEpsYAnio(int anio);
}