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

/**
 * Controlador REST para la gestión de médicos en MediCore.
 * Permite consultar, crear, actualizar y eliminar médicos del sistema.
 */
@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController   {
    private final IMedicoService medicoService;
    private final IEspecialidadService especialidadService;
    private final ICiudadService ciudadService;

    /**
     * Obtiene la lista de todos los médicos registrados en el sistema.
     *
     * @return lista completa de médicos
     */
    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> findAll() {
        List<MedicoResponseDTO> response = medicoService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene únicamente los médicos con estado activo.
     *
     * @return lista de médicos activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<MedicoResponseDTO>> findAllActivos() {
        List<MedicoResponseDTO> response = medicoService.findAll()
                .stream()
                .filter(medico -> Boolean.TRUE.equals(medico.getEstado()))
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Busca un médico por su número de documento.
     *
     * @param documento número de documento del médico
     * @return datos del médico o 404 si no existe
     */
    @GetMapping("/{documento}")
    public ResponseEntity<MedicoResponseDTO> findById(@PathVariable String documento) {
        return medicoService.findById(documento)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Registra un nuevo médico en el sistema.
     *
     * @param request datos del médico a crear, incluyendo especialidad y ciudad
     * @return datos del médico registrado
     */
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

    /**
     * Actualiza los datos de un médico existente.
     *
     * @param documento número de documento del médico a actualizar
     * @param request   nuevos datos del médico
     * @return datos actualizados del médico o 404 si no existe
     */
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

    /**
     * Elimina un médico del sistema por su número de documento.
     *
     * @param documento número de documento del médico a eliminar
     * @return 204 si fue eliminado, 404 si no existe
     */
    @DeleteMapping("/{documento}")
    public ResponseEntity<Void> delete(@PathVariable String documento) {
        return medicoService.delete(documento)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Convierte una entidad {@link Medico} al DTO de respuesta.
     *
     * @param medico entidad del médico a convertir
     * @return DTO con los datos del médico
     */
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

        response.setCodigoCiudad(
                medico.getCiudad() != null ? medico.getCiudad().getCodigo() : null
        );

        return response;
    }

}
