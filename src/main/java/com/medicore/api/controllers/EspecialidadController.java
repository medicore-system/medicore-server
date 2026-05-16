package com.medicore.api.controllers;

import com.medicore.api.dtos.doctor.EspecialidadResponseDTO;
import com.medicore.api.dtos.doctor.MedicoResponseDTO;
import com.medicore.api.entities.Especialidad;
import com.medicore.api.entities.Medico;
import com.medicore.api.services.IEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/especialidad")
@RequiredArgsConstructor
public class EspecialidadController {
    private final IEspecialidadService especialidadService;
    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> findAll() {
        List<EspecialidadResponseDTO> response = especialidadService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    private EspecialidadResponseDTO toResponse(Especialidad especialidad) {
        EspecialidadResponseDTO response = new EspecialidadResponseDTO();

        response.setId(especialidad.getId());
        response.setNombre(especialidad.getNombre());


        return response;
    }
}
