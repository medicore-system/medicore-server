package com.medicore.api.controllers;

import com.medicore.api.dtos.historial.HistorialRequest;
import com.medicore.api.dtos.historial.HistorialResponse;
import com.medicore.api.services.IHistorialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints de gestión de historiales clínicos.
 *
 * <p>Permite al personal médico registrar nuevos historiales y listar los existentes.</p>
 */
@RestController
@RequestMapping("/historial")
@RequiredArgsConstructor
public class HistorialController {

    private final IHistorialService historialService;

    /**
     * Lista todos los historiales clínicos registrados.
     *
     * @return lista de historiales con datos de paciente y médico.
     */
    @GetMapping
    public ResponseEntity<List<HistorialResponse>> listar() {
        return ResponseEntity.ok(historialService.listarHistoriales());
    }

    /**
     * Crea un nuevo historial clínico.
     *
     * @param request datos del historial: código, fecha, tipo, descripción,
     *                documento del paciente y documento del médico.
     * @return historial creado con estado HTTP 201.
     */
    @PostMapping
    public ResponseEntity<HistorialResponse> crear(@Valid @RequestBody HistorialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(historialService.crearHistorial(request));
    }
}
