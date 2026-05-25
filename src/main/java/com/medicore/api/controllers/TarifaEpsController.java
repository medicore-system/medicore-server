package com.medicore.api.controllers;

import com.medicore.api.dtos.tarifa.TarifaEpsRequestDTO;
import com.medicore.api.dtos.tarifa.TarifaEpsResponseDTO;
import com.medicore.api.services.ITarifaEpsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de tarifas por EPS en MediCore.
 * Permite crear, consultar y actualizar las tarifas de servicios asociadas a cada EPS.
 */
@RestController
@RequestMapping("/api/tarifas-eps")
@RequiredArgsConstructor
public class TarifaEpsController {

    private final ITarifaEpsService tarifaEpsService;

    /**
     * Crea una nueva tarifa EPS con los datos del request.
     *
     * @param request datos de la tarifa a crear
     * @return tarifa creada con estado HTTP 201
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TarifaEpsResponseDTO> crearTarifa(@Valid @RequestBody TarifaEpsRequestDTO request) {
        TarifaEpsResponseDTO response = tarifaEpsService.crearTarifa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene todas las tarifas activas asociadas a una EPS específica.
     *
     * @param codigoEps código de la EPS
     * @return lista de tarifas activas de la EPS
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/eps/{codigoEps}")
    public ResponseEntity<List<TarifaEpsResponseDTO>> obtenerTarifasPorEps(@PathVariable String codigoEps) {
        List<TarifaEpsResponseDTO> response = tarifaEpsService.obtenerTarifasActivasPorEps(codigoEps);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza una tarifa EPS existente con los nuevos datos.
     *
     * @param codigo  código de la tarifa a actualizar
     * @param request nuevos datos de la tarifa
     * @return tarifa actualizada
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{codigo}")
    public ResponseEntity<TarifaEpsResponseDTO> actualizarTarifa(
            @PathVariable String codigo,
            @Valid @RequestBody TarifaEpsRequestDTO request) {
        TarifaEpsResponseDTO response = tarifaEpsService.actualizarTarifa(codigo, request);
        return ResponseEntity.ok(response);
    }
}