package com.medicore.api.controllers;

import com.medicore.api.dtos.CiudadRequest;
import com.medicore.api.dtos.CiudadResponse;
import com.medicore.api.services.ICiudadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CiudadController {

    private final ICiudadService ciudadService;

    @GetMapping
    public ResponseEntity<List<CiudadResponse>> listar(
            @RequestParam(required = false) String buscar) {

        List<CiudadResponse> ciudades = (buscar != null && !buscar.isBlank())
                ? ciudadService.buscarCiudadesPorNombre(buscar)
                : ciudadService.listarCiudades();

        return ResponseEntity.ok(ciudades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CiudadResponse> obtener(@PathVariable String id) {
        return ResponseEntity.ok(ciudadService.obtenerCiudad(id));
    }

    @PostMapping
    public ResponseEntity<CiudadResponse> crear(
            @Valid @RequestBody CiudadRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ciudadService.crearCiudad(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CiudadResponse> editar(
            @PathVariable String id,
            @Valid @RequestBody CiudadRequest request) {

        return ResponseEntity.ok(ciudadService.editarCiudad(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        ciudadService.eliminarCiudad(id);
        return ResponseEntity.noContent().build();
    }
}
