package com.medicore.api.controllers.cita;

import com.medicore.api.dtos.Usuario.UsuarioResponseDTO;
import com.medicore.api.dtos.doctor.MedicoRequestDTO;
import com.medicore.api.dtos.doctor.MedicoResponseDTO;
import com.medicore.api.dtos.notificacionCita.NotificacionCitaRequestDTO;
import com.medicore.api.dtos.notificacionCita.NotificacionCitaResponseDTO;
import com.medicore.api.entities.Cita.Cita;
import com.medicore.api.entities.Cita.NotificacionCita;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Especialidad;
import com.medicore.api.entities.Medico;
import com.medicore.api.repositories.cita.ICitaRepository;
import com.medicore.api.services.ICitaService;
import com.medicore.api.services.INotificacionCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/**
 * Controlador REST para la gestión de notificaciones de citas en MediCore.
 * Permite crear, consultar y eliminar notificaciones asociadas a citas médicas.
 */
@RestController
@RequestMapping("notificacion")
@RequiredArgsConstructor
public class NotificacionCitaController {

    private final INotificacionCitaService notificacionCitaService;
    private final ICitaRepository citaRepository;

    /**
     * Crea y guarda una nueva notificación de cita.
     *
     * @param request datos de la notificación incluyendo correo destino y código de cita
     * @return notificación creada
     */
    @PostMapping
    public ResponseEntity<NotificacionCitaResponseDTO> save(@RequestBody NotificacionCitaRequestDTO request) {

        NotificacionCita notificacionCita = new NotificacionCita();
        notificacionCita.setCorreo(request.getCorreoDestino());
        notificacionCita.setDescripcion(request.getDescripcion());
        Cita cita = citaRepository
                .findById(request.getCodigoCita())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        notificacionCita.setCita(cita);
        return ResponseEntity.ok(
                toResponse(notificacionCitaService.save(notificacionCita))
        );
    }

    /**
     * Obtiene las notificaciones pendientes (no leídas) asociadas a un correo electrónico.
     *
     * @param correo dirección de correo del destinatario
     * @return lista de notificaciones con estado pendiente
     */
    @GetMapping("{correo}")
    public ResponseEntity<List<NotificacionCitaResponseDTO>> findByCorreo(@PathVariable String correo){
         List<NotificacionCitaResponseDTO> response= notificacionCitaService.findByCorreo(correo).stream()
                .map(this::toResponse)
                 .filter(nc -> nc.getEstado().equals(false))
                .toList();
         return ResponseEntity.ok(response);
    }

    /**
     * Elimina una notificación de cita por su código.
     *
     * @param codigo identificador de la notificación a eliminar
     * @return 204 si fue eliminada exitosamente, 404 si no existe
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable Integer codigo) {
        return notificacionCitaService.delete(codigo)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Convierte una entidad {@link NotificacionCita} al DTO de respuesta con información de la cita.
     *
     * @param notificacionCita entidad a convertir
     * @return DTO con los datos de la notificación
     */
    private NotificacionCitaResponseDTO toResponse(NotificacionCita  notificacionCita){
        NotificacionCitaResponseDTO responseDTO = new NotificacionCitaResponseDTO();
        responseDTO.setCodigo(notificacionCita.getCodigo());
        responseDTO.setEstado(notificacionCita.getEstado());
        responseDTO.setDescripcion(notificacionCita.getDescripcion());
        responseDTO.setCorreoDestino(notificacionCita.getCorreo());
        if(notificacionCita.getCita() != null){
            responseDTO.setCodigoCita(notificacionCita.getCita().getCodigo() + " - " +notificacionCita.getCita().getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " "
                    + notificacionCita.getCita().getFecha().getDayOfMonth() + " de "
                    + notificacionCita.getCita().getFecha().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " - "
                    + notificacionCita.getCita().getFecha().getHour() + ":" + notificacionCita.getCita().getFecha().getMinute()
                    + " - " + notificacionCita.getCita().getEspecialidad().getNombre());
            responseDTO.setEstadoCita(notificacionCita.getCita().getEstado());
        }
        return responseDTO;
    }

}
