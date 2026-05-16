package com.medicore.api.controllers;

import com.medicore.api.dtos.doctor.EspecialidadResponseDTO;
import com.medicore.api.entities.Especialidad;
import com.medicore.api.services.IEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con las especialidades médicas.
 *
 * <p>
 * Permite consultar las especialidades registradas
 * en el sistema.
 * </p>
 *
 * Base URL: /especialidad
 */
@RestController
@RequestMapping("/especialidad")
@RequiredArgsConstructor
public class EspecialidadController {
    /**
     * Servicio encargado de la lógica de especialidades.
     */
    private final IEspecialidadService especialidadService;

    /**
     * Obtiene la lista de especialidades registradas.
     *
     * @return lista de especialidades en formato DTO
     */
    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> findAll() {
        List<EspecialidadResponseDTO> response = especialidadService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
    /**
     * Convierte una entidad Especialidad en un DTO de respuesta.
     *
     * @param especialidad entidad especialidad
     * @return objeto EspecialidadResponseDTO
     */
    private EspecialidadResponseDTO toResponse(Especialidad especialidad) {
        EspecialidadResponseDTO response = new EspecialidadResponseDTO();

        response.setId(especialidad.getId());
        response.setNombre(especialidad.getNombre());


        return response;
    }
}
