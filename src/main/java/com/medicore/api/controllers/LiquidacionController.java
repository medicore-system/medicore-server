package com.medicore.api.controllers;

import com.medicore.api.dtos.liquidacion.LiquidacionRequestDTO;
import com.medicore.api.dtos.liquidacion.LiquidacionResponseDTO;
import com.medicore.api.services.ILiquidacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @GetMapping
    public ResponseEntity<List<LiquidacionResponseDTO>> obtenerHistorialLiquidaciones() {
        List<LiquidacionResponseDTO> response = liquidacionService.obtenerTodas();

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 si no hay historial
        }

        return ResponseEntity.ok(response); // Retorna 200 con la lista JSON
    }

    @PutMapping("/{codigo}/estado")
    public ResponseEntity<LiquidacionResponseDTO> actualizarEstado(
            @PathVariable String codigo,
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(liquidacionService.cambiarEstado(codigo, nuevoEstado));
    }

    @GetMapping(value = "/{codigo}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarPdf(@PathVariable String codigo) {
        byte[] pdfBytes = liquidacionService.generarPdfLiquidacion(codigo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Le indicamos al navegador/cliente que es un archivo para descargar
        headers.setContentDispositionFormData("attachment", "Liquidacion_" + codigo + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}