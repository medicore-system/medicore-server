package com.medicore.api.controllers.doctor;

import com.medicore.api.dtos.doctor.MedicoResponseDTO;
import com.medicore.api.entities.Medico;
import com.medicore.api.services.IMedicoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class MedicoController   {
    private final IMedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> findAll() {
        List<MedicoResponseDTO> response = medicoService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{documento}")
    public ResponseEntity<MedicoResponseDTO> findById(@PathVariable String documento) {
        return medicoService.findById(documento)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    private MedicoResponseDTO toResponse(Medico medico) {
        MedicoResponseDTO response = new MedicoResponseDTO();

        response.setDocument(medico.getDocumento());
        response.setName(medico.getNombre());
        response.setLastName(medico.getApellido());
        response.setPhone(medico.getTelefono());
        response.setEmail(medico.getCorreo());

        response.setSpecialty(medico.getEspecialidad().getNombre());
        response.setStatus(medico.getEstado() ? "ACTIVE" : "INACTIVE");

        return response;
    }

}
