package com.medicore.api.controllers.AsignacionMedico;

import com.medicore.api.dtos.AsignacionMedico.AsignacionMedicoRequestDTO;
import com.medicore.api.dtos.AsignacionMedico.AsignacionMedicoResponseDTO;
import com.medicore.api.entities.AsignacionMedico;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.HorarioMedico;
import com.medicore.api.entities.Medico;
import com.medicore.api.entities.hospital.Hospital;
import com.medicore.api.repositories.IMedicoRepository;
import com.medicore.api.repositories.hospital.HospitalRepository;
import com.medicore.api.services.IAsignacionMedicoService;
import com.medicore.api.services.ICiudadService;
import com.medicore.api.services.IHospitalService;
import com.medicore.api.services.IMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de asignaciones de médicos a hospitales en MediCore.
 * Permite crear, consultar y desactivar asignaciones médicas con lógica de rotación automática.
 */
@RestController
@RequestMapping("/asignaciones")
@RequiredArgsConstructor
public class AsignacionMedicoController {

    private final IAsignacionMedicoService asignacionMedicoService;
    private final IMedicoService medicoService;
    private final ICiudadService ciudadService;
    private final HospitalRepository hospitalRepository;

    /**
     * Crea una nueva asignación de médico a un hospital.
     * Calcula automáticamente las fechas, el hospital (round-robin si no se especifica)
     * y el horario basado en el historial del médico.
     *
     * @param request datos de la asignación incluyendo documento del médico y ciudad
     * @return asignación creada con estado HTTP 201
     */
    @PostMapping
    public ResponseEntity<AsignacionMedicoResponseDTO> save(@RequestBody AsignacionMedicoRequestDTO request) {

        Medico medico = medicoService.findById(request.getDocumentoMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado: " + request.getDocumentoMedico()));

        if (!medico.getEstado()) {
            throw new RuntimeException("El médico está inactivo y no puede ser asignado");
        }

        if (!medico.getCiudad().getCodigo().equals(request.getCodigoCiudad())) {
            throw new RuntimeException("El médico no pertenece a la ciudad indicada");
        }

        Ciudad ciudad = ciudadService.findById(request.getCodigoCiudad())
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada: " + request.getCodigoCiudad()));

        // Fechas automáticas basadas en el último turno del médico
        LocalDate fechaInicio = asignacionMedicoService.findTopByMedicoOrderByFechaFinDesc(request.getDocumentoMedico())
                .map(t -> t.getFechaFin().plusDays(1))
                .orElse(LocalDate.now());

        LocalDate fechaFin = fechaInicio.plusDays(request.getDuracionDias() - 1);

        // Hospital: manual si viene en el request, round-robin si no
        Hospital hospital;
        if (request.getCodigoHospital() != null && !request.getCodigoHospital().isBlank()) {
            hospital = hospitalRepository.findById(request.getCodigoHospital())
                    .orElseThrow(() -> new RuntimeException("Hospital no encontrado: " + request.getCodigoHospital()));
            if (!hospital.getCiudad().getCodigo().equals(request.getCodigoCiudad())) {
                throw new RuntimeException("El hospital no pertenece a la ciudad indicada");
            }
        } else {
            hospital = asignacionMedicoService.determinarSiguienteHospital(request.getCodigoCiudad());
        }

        // Horario automático según historial personal del médico
        HorarioMedico horario = asignacionMedicoService.determinarSiguienteHorario(request.getDocumentoMedico());

        AsignacionMedico asignacion = new AsignacionMedico();
        asignacion.setFechaInicio(fechaInicio);
        asignacion.setFechaFin(fechaFin);
        asignacion.setMedico(medico);
        asignacion.setHospital(hospital);
        asignacion.setCiudad(ciudad);
        asignacion.setHorario(horario);
        asignacion.setEstado(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                toResponse(asignacionMedicoService.save(asignacion))
        );
    }

    /**
     * Consulta todas las asignaciones de un médico por su número de documento.
     *
     * @param documento número de documento del médico
     * @return lista de asignaciones del médico
     */
    @GetMapping("/medico/{documento}")
    public ResponseEntity<List<AsignacionMedicoResponseDTO>> findByMedico(@PathVariable String documento) {
        return ResponseEntity.ok(
                asignacionMedicoService.findByMedico(documento)
                        .stream().map(this::toResponse).toList()
        );
    }

    /**
     * Consulta todas las asignaciones activas en un hospital específico.
     *
     * @param codigoHospital código del hospital
     * @return lista de asignaciones del hospital
     */
    @GetMapping("/hospital/{codigoHospital}")
    public ResponseEntity<List<AsignacionMedicoResponseDTO>> findByHospital(@PathVariable String codigoHospital) {
        return ResponseEntity.ok(
                asignacionMedicoService.findByHospital(codigoHospital)
                        .stream().map(this::toResponse).toList()
        );
    }

    /**
     * Desactiva una asignación médica por su código.
     *
     * @param codigo identificador de la asignación a desactivar
     * @return 204 si fue desactivada, 404 si no existe
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> desactivar(@PathVariable Integer codigo) {
        return asignacionMedicoService.desactivar(codigo)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Convierte una entidad {@link AsignacionMedico} al DTO de respuesta.
     *
     * @param a entidad de asignación a convertir
     * @return DTO con los datos de la asignación
     */
    private AsignacionMedicoResponseDTO toResponse(AsignacionMedico a) {
        AsignacionMedicoResponseDTO dto = new AsignacionMedicoResponseDTO();
        dto.setCodigo(a.getCodigo());
        dto.setFechaInicio(a.getFechaInicio());
        dto.setFechaFin(a.getFechaFin());
        dto.setNombreMedico(a.getMedico().getNombre() + " " + a.getMedico().getApellido());
        dto.setNombreCiudad(a.getCiudad().getNombre());
        dto.setNombreHospital(a.getHospital().getNombre());
        dto.setHorario(a.getHorario().getHorario());
        dto.setEstado(a.getEstado());
        return dto;
    }
}
