package com.medicore.api.controllers;

import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
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
            return ResponseEntity.noContent().build(); // Retorna 204 si no hay datos para ese año
        }
        
        return ResponseEntity.ok(reporte); // Retorna 200 OK con la lista
    }
}