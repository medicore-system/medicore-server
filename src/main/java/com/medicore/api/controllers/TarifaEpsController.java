package com.medicore.api.controllers;

import com.medicore.api.dtos.tarifa.TarifaEpsRequestDTO;
import com.medicore.api.dtos.tarifa.TarifaEpsResponseDTO;
import com.medicore.api.services.ITarifaEpsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifas-eps")
@RequiredArgsConstructor
public class TarifaEpsController {

    private final ITarifaEpsService tarifaEpsService;

    @PostMapping
    public ResponseEntity<TarifaEpsResponseDTO> crearTarifa(@Valid @RequestBody TarifaEpsRequestDTO request) {
        TarifaEpsResponseDTO response = tarifaEpsService.crearTarifa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/eps/{codigoEps}")
    public ResponseEntity<List<TarifaEpsResponseDTO>> obtenerTarifasPorEps(@PathVariable String codigoEps) {
        List<TarifaEpsResponseDTO> response = tarifaEpsService.obtenerTarifasActivasPorEps(codigoEps);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<TarifaEpsResponseDTO> actualizarTarifa(
            @PathVariable String codigo,
            @Valid @RequestBody TarifaEpsRequestDTO request) {
        TarifaEpsResponseDTO response = tarifaEpsService.actualizarTarifa(codigo, request);
        return ResponseEntity.ok(response);
    }
}