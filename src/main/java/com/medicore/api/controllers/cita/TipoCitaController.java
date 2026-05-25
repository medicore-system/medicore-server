package com.medicore.api.controllers.cita;

import com.medicore.api.dtos.tipoCita.TipoCitaResponseDTO;
import com.medicore.api.entities.Cita.TipoCita;
import com.medicore.api.services.ITipoCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con los tipoCita del sistema.
 *
 * <p>Permite consultar tipos de citas.</p>
 *
 * Base URL: /tipoCita
 *
 * @author Manuel
 */
@RestController
@RequestMapping("/tipoCita")
@RequiredArgsConstructor
public class TipoCitaController {

    /**
     * Servicio encargado de la lógica de negocio de tipoCita.
     */
    private final ITipoCitaService tipoCitaService;

    /**
     * Obtiene todos los tipos de cita registrados.
     *
     * @return lista de tipos de cita en formato DTO.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
    @GetMapping
    public ResponseEntity<List<TipoCitaResponseDTO>> findAll(){
        List<TipoCitaResponseDTO> response = tipoCitaService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Convierte una entidad {@link TipoCita} en un DTO de respuesta.
     *
     * <p>Este método encapsula la transformación de entidades
     * hacia objetos de transferencia de datos.</p>
     *
     * @param tipoCita entidad tipoCita.
     * @return DTO de respuesta del tipo de cita.
     */
    private TipoCitaResponseDTO toResponse(TipoCita tipoCita){
        TipoCitaResponseDTO responseDTO = new TipoCitaResponseDTO();
        responseDTO.setId(tipoCita.getId());
        responseDTO.setNombre(tipoCita.getNombre());
        return responseDTO;
    }
}
