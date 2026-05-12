package com.medicore.api.controllers;

import com.medicore.api.dtos.servicio.ServicioDetalleResponse;
import com.medicore.api.dtos.servicio.ServicioRequest;
import com.medicore.api.dtos.servicio.ServicioResponse;
import com.medicore.api.dtos.servicio.TipoServicioResponse;
import com.medicore.api.services.IServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión administrativa de servicios médicos.
 *
 * Endpoints requeridos por Swagger:
 * GET    /services
 * POST   /services
 * GET    /services/{id}
 * PUT    /services/{id}
 * DELETE /services/{id}
 */
@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServicioController {

    private final IServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<ServicioResponse>> getServices(
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(servicioService.buscarServicios(search));
    }

    @PostMapping
    public ResponseEntity<ServicioResponse> createService(
            @Valid @RequestBody ServicioRequest request
    ) {
        ServicioResponse response = servicioService.crearServicio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDetalleResponse> getServiceById(
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(servicioService.obtenerDetalleServicio(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponse> updateService(
            @PathVariable("id") String id,
            @Valid @RequestBody ServicioRequest request
    ) {
        return ResponseEntity.ok(servicioService.editarServicio(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
            @PathVariable("id") String id
    ) {
        servicioService.inactivarServicio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/types")
    public ResponseEntity<List<TipoServicioResponse>> getServiceTypes() {
        return ResponseEntity.ok(servicioService.listarTiposServicio());
    }
}