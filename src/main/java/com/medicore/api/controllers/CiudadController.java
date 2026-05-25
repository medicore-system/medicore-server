package com.medicore.api.controllers;

import com.medicore.api.dtos.ciudad.CiudadRequestDTO;
import com.medicore.api.dtos.ciudad.CiudadResponseDTO;
import com.medicore.api.services.ICiudadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de ciudades del sistema MediCore.
 * Permite listar, consultar, crear, editar y eliminar ciudades.
 */
@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CiudadController {

    private final ICiudadService ciudadService;

    /**
     * Lista todas las ciudades, con opción de filtrar por nombre.
     *
     * @param buscar texto opcional para filtrar ciudades por nombre
     * @return lista de ciudades que coinciden con el criterio de búsqueda
     * --
     */
    @GetMapping
    public ResponseEntity<List<CiudadResponseDTO>> listar(
            @RequestParam(required = false) String buscar) {

        List<CiudadResponseDTO> ciudades = (buscar != null && !buscar.isBlank())
                ? ciudadService.buscarCiudadesPorNombre(buscar)
                : ciudadService.listarCiudades();

        return ResponseEntity.ok(ciudades);
    }

    /**
     * Obtiene una ciudad por su identificador.
     *
     * @param id código único de la ciudad
     * @return datos de la ciudad encontrada
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> obtener(@PathVariable String id) {
        return ResponseEntity.ok(ciudadService.obtenerCiudad(id));
    }

    /**
     * Crea una nueva ciudad con los datos del request.
     *
     * @param request datos de la ciudad a crear
     * @return ciudad creada con estado HTTP 201
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CiudadResponseDTO> crear(
            @Valid @RequestBody CiudadRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ciudadService.crearCiudad(request));
    }

    /**
     * Actualiza los datos de una ciudad existente.
     *
     * @param id      código de la ciudad a actualizar
     * @param request nuevos datos de la ciudad
     * @return ciudad actualizada
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> editar(
            @PathVariable String id,
            @Valid @RequestBody CiudadRequestDTO request) {

        return ResponseEntity.ok(ciudadService.editarCiudad(id, request));
    }

    /**
     * Elimina una ciudad por su identificador.
     *
     * @param id código de la ciudad a eliminar
     * @return respuesta vacía con estado HTTP 204
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        ciudadService.eliminarCiudad(id);
        return ResponseEntity.noContent().build();
    }
}
