package com.medicore.api.controllers;

import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import com.medicore.api.dtos.reportes.EstadoCarteraDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.dtos.reportes.ProductividadMedicoDTO;
import com.medicore.api.services.IReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportesController {

    private final IReporteService reporteService;

    @GetMapping("/ingresos-hospital")
    public ResponseEntity<List<IngresosHospitalDTO>> obtenerIngresosHospital(
            @RequestParam(name = "anio") int anio) {

        List<IngresosHospitalDTO> reporte = reporteService.obtenerIngresosPorHospital(anio);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/ingresos-especialidad")
    public ResponseEntity<List<IngresosEspecialidadDTO>> obtenerIngresosEspecialidad(
            @RequestParam(name = "anio") int anio) {

        List<IngresosEspecialidadDTO> reporte = reporteService.obtenerIngresosPorEspecialidad(anio);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/atenciones-eps")
    public ResponseEntity<List<AtencionesEpsDTO>> obtenerAtencionesEps(
            @RequestParam(name = "anio") int anio) {

        List<AtencionesEpsDTO> reporte = reporteService.obtenerAtencionesPorEps(anio);
 
        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reporte);
    }

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

    @GetMapping("/estado-cartera")
    public ResponseEntity<List<EstadoCarteraDTO>> obtenerEstadoCartera() {
        return ResponseEntity.ok(reporteService.obtenerEstadoCartera());
    }
}