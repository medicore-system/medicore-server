package com.medicore.api.controllers;

import com.medicore.api.dtos.ciudad.CiudadRequestDTO;
import com.medicore.api.dtos.ciudad.CiudadResponseDTO;
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
    public ResponseEntity<List<CiudadResponseDTO>> listar(
            @RequestParam(required = false) String buscar) {

        List<CiudadResponseDTO> ciudades = (buscar != null && !buscar.isBlank())
                ? ciudadService.buscarCiudadesPorNombre(buscar)
                : ciudadService.listarCiudades();

        return ResponseEntity.ok(ciudades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> obtener(@PathVariable String id) {
        return ResponseEntity.ok(ciudadService.obtenerCiudad(id));
    }

    @PostMapping
    public ResponseEntity<CiudadResponseDTO> crear(
            @Valid @RequestBody CiudadRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ciudadService.crearCiudad(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> editar(
            @PathVariable String id,
            @Valid @RequestBody CiudadRequestDTO request) {

        return ResponseEntity.ok(ciudadService.editarCiudad(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        ciudadService.eliminarCiudad(id);
        return ResponseEntity.noContent().build();
    }
}
