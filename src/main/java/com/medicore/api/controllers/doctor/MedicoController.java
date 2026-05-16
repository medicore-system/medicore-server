package com.medicore.api.controllers.doctor;

import com.medicore.api.dtos.doctor.MedicoRequestDTO;
import com.medicore.api.dtos.doctor.MedicoResponseDTO;
import com.medicore.api.dtos.doctor.MedicoUpdateDTO;
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
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con los médicos del sistema.
 *
 * <p>Permite consultar, registrar, actualizar e
 * inhabilitar médicos.</p>
 *
 * Base URL: /Medicos
 *
 * @author Camila Prada
 */
@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController   {
    /**
     * Servicio encargado de la lógica de médicos.
     */
    private final IMedicoService medicoService;

    /**
     * Servicio encargado de la lógica de especialidades.
     */
    private final IEspecialidadService especialidadService;

    /**
     * Servicio encargado de la lógica de ciudades.
     */
    private final ICiudadService ciudadService;

    /**
     * Obtiene la lista de médicos activos registrados.
     *
     * @return lista de médicos en formato DTO
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
     * Busca un médico por su documento.
     *
     * @param documento documento del médico
     * @return médico encontrado o respuesta 404
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
     * @param request datos del médico a registrar
     * @return médico registrado en formato DTO
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
     * Actualiza la información de un médico existente.
     *
     * @param documento documento del médico a actualizar
     * @param request nuevos datos del médico
     * @return médico actualizado o respuesta 404
     */
        @PutMapping("/{documento}")
        public ResponseEntity<MedicoResponseDTO> update(@PathVariable String documento,
                                                        @RequestBody MedicoUpdateDTO request) {

            Medico medico = new Medico();
            medico.setNombre(request.getNombre());
            medico.setApellido(request.getApellido());
            Especialidad e = especialidadService.findById(request.getIdEspecialidad())
                    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
            medico.setEspecialidad(e);
            medico.setTelefono(request.getTelefono());
            medico.setEmail(request.getEmail());
            Ciudad c = ciudadService.findById(request.getCodigoCiudad())
                    .orElseThrow(()-> new RuntimeException("Ciudad no encontrada"));
            medico.setCiudad(c);
            return medicoService.update(documento, medico)
                    .map(this::toResponse)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
    /**
     * Inhabilita un médico mediante su documento.
     *
     * @param documento documento del médico
     * @return respuesta vacía indicando el resultado de la operación
     */
    @DeleteMapping("/{documento}")
    public ResponseEntity<Void> delete(@PathVariable String documento) {
        return medicoService.delete(documento)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    /**
     * Convierte una entidad Medico en un DTO de respuesta.
     *
     * @param medico entidad médico
     * @return objeto MedicoResponseDTO
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

        return response;
    }

}
