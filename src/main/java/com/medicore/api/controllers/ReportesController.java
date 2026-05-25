package com.medicore.api.controllers;

import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import com.medicore.api.dtos.reportes.EstadoCarteraDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.dtos.reportes.ProductividadMedicoDTO;
import com.medicore.api.services.IReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la generación de reportes gerenciales en MediCore.
 * Expone endpoints para consultar ingresos, productividad médica y estado de cartera.
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportesController {

    private final IReporteService reporteService;

    /**
     * Obtiene el reporte de ingresos agrupados por hospital para un año dado.
     *
     * @param anio año para el cual se genera el reporte
     * @return lista de ingresos por hospital, o 204 si no hay datos
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ingresos-hospital")
    public ResponseEntity<List<IngresosHospitalDTO>> obtenerIngresosHospital(
            @RequestParam(name = "anio") int anio) {

        List<IngresosHospitalDTO> reporte = reporteService.obtenerIngresosPorHospital(anio);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reporte);
    }

    /**
     * Obtiene el reporte de ingresos agrupados por especialidad médica para un año dado.
     *
     * @param anio año para el cual se genera el reporte
     * @return lista de ingresos por especialidad, o 204 si no hay datos
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ingresos-especialidad")
    public ResponseEntity<List<IngresosEspecialidadDTO>> obtenerIngresosEspecialidad(
            @RequestParam(name = "anio") int anio) {

        List<IngresosEspecialidadDTO> reporte = reporteService.obtenerIngresosPorEspecialidad(anio);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reporte);
    }

    /**
     * Obtiene el reporte de atenciones agrupadas por EPS para un año dado.
     *
     * @param anio año para el cual se genera el reporte
     * @return lista de atenciones por EPS, o 204 si no hay datos
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/atenciones-eps")
    public ResponseEntity<List<AtencionesEpsDTO>> obtenerAtencionesEps(
            @RequestParam(name = "anio") int anio) {

        List<AtencionesEpsDTO> reporte = reporteService.obtenerAtencionesPorEps(anio);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reporte);
    }

    /**
     * Obtiene el reporte de productividad médica por mes y año.
     *
     * @param anio año del reporte
     * @param mes  mes del reporte
     * @return lista de productividad por médico, o 204 si no hay datos
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/productividad-medica")
    public ResponseEntity<List<ProductividadMedicoDTO>> obtenerProductividadMedica(
            @RequestParam(name = "anio") int anio,
            @RequestParam(name = "mes") int mes) {

        List<ProductividadMedicoDTO> reporte = reporteService.obtenerProductividadMedica(anio, mes);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reporte);
    }

    /**
     * Obtiene el estado actual de la cartera de cuentas por cobrar.
     *
     * @return lista del estado de cartera, o 204 si no hay datos
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/estado-cartera")
    public ResponseEntity<List<EstadoCarteraDTO>> obtenerEstadoCartera() {
        
        List<EstadoCarteraDTO> reporte = reporteService.obtenerEstadoCartera();
        
        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(reporte);
    }
}