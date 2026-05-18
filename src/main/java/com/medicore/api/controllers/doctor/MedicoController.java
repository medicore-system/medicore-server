package com.medicore.api.controllers.doctor;

import com.medicore.api.dtos.doctor.MedicoRequestDTO;
import com.medicore.api.dtos.doctor.MedicoResponseDTO;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Especialidad;
import com.medicore.api.entities.Medico;
import com.medicore.api.services.ICiudadService;
import com.medicore.api.services.IEspecialidadService;
import com.medicore.api.services.IMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController   {
    private final IMedicoService medicoService;
    private final IEspecialidadService especialidadService;
    private final ICiudadService ciudadService;

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> findAll() {
        List<MedicoResponseDTO> response = medicoService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<MedicoResponseDTO>> findAllActivos() {
        List<MedicoResponseDTO> response = medicoService.findAll()
                .stream()
                .filter(medico -> Boolean.TRUE.equals(medico.getEstado()))
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

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> save(@RequestBody MedicoRequestDTO request) {

        Medico medico = new Medico();
        medico.setDocumento(request.getDocumento());
        medico.setNombre(request.getNombre());
        medico.setApellido(request.getApellido());
        medico.setEmail(request.getEmail());
        medico.setTelefono(request.getTelefono());
        medico.setEstado(true);

        Especialidad especialidad = especialidadService
                .findById(request.getIdEspecialidad())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        medico.setEspecialidad(especialidad);

        Ciudad ciudad = ciudadService
                .findById(request.getCodigoCiudad())
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));

        medico.setCiudad(ciudad);

        return ResponseEntity.ok(
                toResponse(medicoService.save(medico))
        );
    }

        @PutMapping("/{documento}")
        public ResponseEntity<MedicoResponseDTO> update(@PathVariable String documento,
                                                        @RequestBody MedicoRequestDTO request) {

            Medico medico = new Medico();

            medico.setDocumento(request.getDocumento());
            medico.setNombre(request.getNombre());
            medico.setApellido(request.getApellido());
            medico.setTelefono(request.getTelefono());
            medico.setEmail(request.getEmail());
            medico.setEstado(request.getEstado());
            // Especialidad
            Especialidad especialidad = new Especialidad();
            especialidad.setId(request.getIdEspecialidad());

            medico.setEspecialidad(especialidad);

            // Ciudad
            Ciudad ciudad = new Ciudad();
            ciudad.setCodigo(request.getCodigoCiudad());

            medico.setCiudad(ciudad);
            return medicoService.update(documento, medico)
                    .map(this::toResponse)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

    @DeleteMapping("/{documento}")
    public ResponseEntity<Void> delete(@PathVariable String documento) {
        return medicoService.delete(documento)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private MedicoResponseDTO toResponse(Medico medico) {
        MedicoResponseDTO response = new MedicoResponseDTO();

        response.setDocumento(medico.getDocumento());
        response.setNombre(medico.getNombre());
        response.setApellido(medico.getApellido());
        response.setTelefono(medico.getTelefono());
        response.setEmail(medico.getEmail());

        response.setNombreEspecialidad(medico.getEspecialidad().getNombre());
        response.setStatus(medico.getEstado() ? "ACTIVE" : "INACTIVE");


        response.setNombreCiudad(
                medico.getCiudad() != null ? medico.getCiudad().getNombre() : null
        );

        return response;
    }

}
