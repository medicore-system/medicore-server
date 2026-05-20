package com.medicore.api.services;

import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import com.medicore.api.dtos.reportes.EstadoCarteraDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.dtos.reportes.ProductividadMedicoDTO;

import java.util.List;

/**
 * Interfaz de servicio que define las operaciones de generación de reportes gerenciales en MediCore.
 */
public interface IReporteService {

    /**
     * Obtiene los ingresos totales por hospital para un año dado.
     *
     * @param anio año del reporte
     * @return lista de ingresos agrupados por hospital
     */
    List<IngresosHospitalDTO> obtenerIngresosPorHospital(int anio);

    /**
     * Obtiene los ingresos totales por especialidad médica para un año dado.
     *
     * @param anio año del reporte
     * @return lista de ingresos agrupados por especialidad
     */
    List<IngresosEspecialidadDTO> obtenerIngresosPorEspecialidad(int anio);

    /**
     * Obtiene el número de atenciones y el monto total por EPS para un año dado.
     *
     * @param anio año del reporte
     * @return lista de atenciones agrupadas por EPS
     */
    List<AtencionesEpsDTO> obtenerAtencionesPorEps(int anio);

    /**
     * Obtiene la productividad médica (citas completadas) por médico en un mes y año dados.
     *
     * @param anio año del reporte
     * @param mes  mes del reporte
     * @return lista de productividad por médico
     */
    List<ProductividadMedicoDTO> obtenerProductividadMedica(int anio, int mes);

    /**
     * Obtiene el estado actual de la cartera de liquidaciones pendientes por EPS.
     *
     * @return lista del estado de cartera por EPS
     */
    List<EstadoCarteraDTO> obtenerEstadoCartera();
}