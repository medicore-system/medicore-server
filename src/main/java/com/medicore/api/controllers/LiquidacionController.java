package com.medicore.api.controllers;

import com.medicore.api.dtos.liquidacion.LiquidacionRequestDTO;
import com.medicore.api.dtos.liquidacion.LiquidacionResponseDTO;
import com.medicore.api.services.ILiquidacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liquidaciones")
@RequiredArgsConstructor
public class LiquidacionController {

    private final ILiquidacionService liquidacionService;

    @PostMapping
    public ResponseEntity<LiquidacionResponseDTO> generarLiquidacion(@RequestBody LiquidacionRequestDTO request) {
        LiquidacionResponseDTO response = liquidacionService.generarLiquidacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}