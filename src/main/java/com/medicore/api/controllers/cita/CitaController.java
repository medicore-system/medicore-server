package com.medicore.api.controllers.cita;

import com.medicore.api.dtos.cita.CitaCreateRequestDTO;
import com.medicore.api.dtos.cita.CitaResponseDTO;
import com.medicore.api.dtos.notificacionCita.NotificacionCitaRequestDTO;
import com.medicore.api.dtos.notificacionCita.NotificacionCitaResponseDTO;
import com.medicore.api.entities.*;
import com.medicore.api.entities.Cita.Cita;
import com.medicore.api.entities.Cita.NotificacionCita;
import com.medicore.api.entities.Cita.TipoCita;
import com.medicore.api.entities.hospital.Hospital;
import com.medicore.api.repositories.cita.ICitaRepository;
import com.medicore.api.repositories.cita.ITipoCitaRepository;
import com.medicore.api.repositories.IUsuarioRepository;
import com.medicore.api.repositories.IMedicoRepository;
import com.medicore.api.repositories.hospital.HospitalRepository;
import com.medicore.api.services.ICitaService;
import com.medicore.api.services.IEspecialidadService;
import com.medicore.api.services.INotificacionCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con las citas médicas.
 *
 * <p>Permite crear, consultar, aprobar y denegar citas.</p>
 *
 * Base URL: /Citas
 *
 * @author Manuel
 */
@RestController
@RequestMapping("/Citas")
@RequiredArgsConstructor
public class CitaController {
    /**
     * Servicio encargado de la lógica de negocio de las citas.
     */
    private final ICitaService citaService;

    private final INotificacionCitaService notificacionCitaService;
    /**
     * Repositorio para operaciones directas sobre citas.
     */
    private final ICitaRepository citaRepository;
    /**
     * Repositorio para la gestión de hospitales.
     */
    private final HospitalRepository hospitalRepository;
    /**
     * Repositorio para la gestión de usuarios.
     */
    private final IUsuarioRepository usuarioRepository;
    /**
     * Repositorio para la gestión de tipos de cita.
     */
    private final ITipoCitaRepository tipoCitaRepository;
    /**
     * Repositorio para la gestión de tipos de cita.
     */
    private final IEspecialidadService especialidadService;
    /**
     * Repositorio para la gestión de medicos.
     */
    private final IMedicoRepository medicoRepository;

    /**
     * Crea una nueva cita médica.
     *
     * <p>CAMBIOS vs versión anterior:
     * <ul>
     *   <li>Corrección de bug: se usaba {@code getId_tipo()} para buscar
     *       la especialidad; ahora usa {@code getId_especialidad()}.</li>
     *   <li>Validación de doble booking: se rechaza si el médico ya
     *       tiene cita en ese mismo slot.</li>
     * </ul>
     * </p>
     *
     * @param cita DTO con la información necesaria para crear la cita.
     * @return ResponseEntity con la cita creada y estado HTTP 201.
     */
    @PostMapping
    public ResponseEntity<CitaResponseDTO> save(@RequestBody CitaCreateRequestDTO cita) {

        Hospital h = hospitalRepository.findById(cita.getCodigo_hospital())
                .orElseThrow(() -> new RuntimeException("Hospital no encontrado"));

        Usuario u = usuarioRepository.findById(cita.getDocumento_paciente())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Especialidad e = especialidadService.findById(cita.getId_especialidad())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        TipoCita tc = tipoCitaRepository.findById(cita.getId_tipo())
                .orElseThrow(() -> new RuntimeException("Tipo de cita no encontrado"));

        Medico m = medicoRepository.findById(cita.getDocumento_medico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (citaRepository.existsByMedicoDocumentoAndFecha(
                cita.getDocumento_medico(), cita.getFecha())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Cita c = new Cita();
        c.setFecha(cita.getFecha());
        c.setCosto(cita.getCosto());
        c.setEspecialidad(e);
        c.setTipoCita(tc);
        c.setUsuario(u);
        c.setMedico(m);
        c.setHospital(h);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(citaService.save(c)));
    }

    /**
     * Obtiene todas las citas registradas.
     *
     * @return lista de citas en formato DTO.
     */
    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> findAll(){
        List<CitaResponseDTO> response = citaService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Busca una cita por el documento del paciente.
     *
     * @param documento_paciente documento del paciente asociado a la cita.
     * @return la cita encontrada o 404 si no existe.
     */
    @GetMapping("/{documento_paciente}")
    public ResponseEntity<List<CitaResponseDTO>> findByDocumento(@PathVariable String documento_paciente){
        List<CitaResponseDTO> response = citaService.findByDocumento(documento_paciente).stream()
                .map(this::toResponse)
                .toList();
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca una cita por el documento del paciente.
     *
     * @param documento_medico documento del paciente asociado a la cita.
     * @return la cita encontrada o 404 si no existe.
     */
    @GetMapping("/medico/{documento_medico}")
    public ResponseEntity<List<CitaResponseDTO>> findByMedico(@PathVariable String documento_medico){
        List<CitaResponseDTO> response = citaService.findByMedico(documento_medico).stream()
                .map(this::toResponse)
                .toList();
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    /**
     * Aprueba una cita médica.
     *
     * <p>El estado de la cita cambia a "APROBADA".</p>
     *
     * @param codigo código único de la cita.
     * @return la cita actualizada o 404 si no existe.
     */
    @PutMapping("/aprobar/{codigo}")
    public ResponseEntity<CitaResponseDTO> aprobar(@PathVariable String codigo){
        return citaService.findByCodigo(codigo)
                .map(existing -> {
                    existing.setEstado("APROBADA");
                    Cita guardada = citaService.save(existing);
                    if(guardada.getUsuario() != null && guardada.getUsuario().getCorreo() != null){
                        crearNotificacion(guardada,
                        "tu Cita en " + guardada.getHospital().getNombre() + " ha sido APROBADA");
                    }
                    return toResponse(guardada);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deniega una cita médica.
     *
     * <p>El estado de la cita cambia a "DENEGADA".</p>
     *
     * @param codigo código único de la cita.
     * @return la cita actualizada o 404 si no existe.
     */
    @PutMapping("/denegar/{codigo}")
    public ResponseEntity<CitaResponseDTO> denegar(@PathVariable String codigo){
        return citaService.findByCodigo(codigo)
                .map(existing -> {
                    existing.setEstado("DENEGADA");
                    Cita guardada = citaService.save(existing);
                    if(guardada.getUsuario() != null && guardada.getUsuario().getCorreo() != null){
                        crearNotificacion(guardada,
                                "tu Cita en " + guardada.getHospital().getNombre() + " ha sido DENEGADA");
                    }
                    return toResponse(guardada);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Convierte una entidad {@link Cita} en un DTO de respuesta.
     *
     * <p>Este método encapsula la lógica de transformación
     * de entidades a objetos de transferencia de datos.</p>
     *
     * @param cita entidad cita.
     * @return DTO con la información de respuesta.
     */
    private CitaResponseDTO toResponse(Cita cita){
        CitaResponseDTO responseDTO = new CitaResponseDTO();
        responseDTO.setCodigo(cita.getCodigo());
        responseDTO.setEstado(cita.getEstado());
        responseDTO.setFecha(cita.getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " "
        + cita.getFecha().getDayOfMonth() + " de "
        + cita.getFecha().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " - "
        + cita.getFecha().getHour() + ":" + cita.getFecha().getMinute());
        responseDTO.setCosto(cita.getCosto());
        if(cita.getEspecialidad()!=null){
            responseDTO.setEspecialidad(cita.getEspecialidad()   .getNombre());
        }
        if(cita.getTipoCita()!=null){
            responseDTO.setTipoCita(cita.getTipoCita().getNombre());
        }
        if(cita.getUsuario()!=null){
            responseDTO.setNombreUsuario(cita.getUsuario().getNombre() + " " +  cita.getUsuario().getApellido());
        }
        if(cita.getHospital()!=null){
            responseDTO.setHospital(cita.getHospital().getNombre());
            responseDTO.setCiudad(cita.getHospital().getCiudad().getNombre());
        }
        if(cita.getMedico()!=null){
            responseDTO.setMedico(cita.getMedico().getNombre() + " " +  cita.getMedico().getApellido());
        }
        return responseDTO;
    }

    // Método privado para no repetir la lógica de creacion de notificaciones
    private void crearNotificacion(Cita cita, String descripcion) {
        NotificacionCita notificacion = new NotificacionCita();
        notificacion.setCorreo(cita.getUsuario().getCorreo());
        notificacion.setDescripcion(descripcion);
        notificacion.setCita(cita);
        notificacionCitaService.save(notificacion);
    }


}
