package com.medicore.api.controllers;

import com.medicore.api.dtos.factura.FacturaCajaDTO;
import com.medicore.api.services.ICajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caja")
@RequiredArgsConstructor
public class CajaController {

    private final ICajaService cajaService;

    @GetMapping("/pendientes/{documento}")
    public ResponseEntity<List<FacturaCajaDTO>> buscarPendientes(@PathVariable String documento) {
        List<FacturaCajaDTO> facturas = cajaService.buscarFacturasPendientesPorPaciente(documento);
        if (facturas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(facturas);
    }

    @PostMapping("/pagar/{codigoFactura}")
    public ResponseEntity<byte[]> procesarPagoCaja(@PathVariable String codigoFactura) {
        byte[] pdfBytes = cajaService.procesarPagoYGenerarRecibo(codigoFactura);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Recibo_Caja_" + codigoFactura + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}