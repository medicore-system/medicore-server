package com.medicore.api.controllers.cita;

import com.medicore.api.dtos.Usuario.UsuarioResponseDTO;
import com.medicore.api.dtos.notificacionCita.NotificacionCitaResponseDTO;
import com.medicore.api.entities.Cita.NotificacionCita;
import com.medicore.api.services.INotificacionCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notificacion")
@RequiredArgsConstructor
public class NotificacionCitaController {

    private final INotificacionCitaService notificacionCitaService;

    @GetMapping("{correo}")
    public ResponseEntity<List<NotificacionCitaResponseDTO>> findByCorreo(@PathVariable String correo){
         List<NotificacionCitaResponseDTO> response= notificacionCitaService.findByCorreo(correo).stream()
                .map(this::toResponse)
                .toList();
         return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable Integer codigo) {
        return notificacionCitaService.delete(codigo)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private NotificacionCitaResponseDTO toResponse(NotificacionCita  notificacionCita){
        NotificacionCitaResponseDTO responseDTO = new NotificacionCitaResponseDTO();
        responseDTO.setCodigo(notificacionCita.getCodigo());
        responseDTO.setEstado(notificacionCita.getEstado());
        responseDTO.setDescripcion(notificacionCita.getDescripcion());
        responseDTO.setCorreoDestino(notificacionCita.getCorreo());
        if(notificacionCita.getCita() != null){
            responseDTO.setCodigoCita(notificacionCita.getCita().getCodigo());
        }
        return responseDTO;
    }

}
