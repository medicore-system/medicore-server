package com.medicore.api.controllers;

import com.medicore.api.dtos.tipoCita.TipoCitaResponseDTO;
import com.medicore.api.entities.TipoCita;
import com.medicore.api.services.ITipoCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con los TipoCita del sistema.
 *
 * <p>Permite consultar Tipos de Cita.</p>
 *
 * Base URL: /TipoCita
 *
 * @author Manuel
 */
@RestController
@RequestMapping("/TipoCita")
@RequiredArgsConstructor
public class TipoCitaController {

    /**
     * Servicio encargado de la lógica de negocio de TipoCita.
     */
    private final ITipoCitaService tipoCitaService;

    /**
     * Obtiene todos los TipoCita registrados.
     *
     * @return lista de TipoCita en formato DTO.
     */
    @GetMapping
    public ResponseEntity<List<TipoCitaResponseDTO>> findAll(){
        List<TipoCitaResponseDTO> response = tipoCitaService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Busca un TipoCita por su Id.
     *
     * @param id id del TipoCita.
     * @return TipoCita encontrado o respuesta 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoCitaResponseDTO> findById(@PathVariable Integer id){
        return tipoCitaService.findById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Convierte una entidad {@link TipoCita} en un DTO de respuesta.
     *
     * <p>Este método encapsula la transformación de entidades
     * hacia objetos de transferencia de datos.</p>
     *
     * @param tc entidad TipoCita.
     * @return DTO de respuesta del TipoCita.
     */
    private TipoCitaResponseDTO toResponse(TipoCita tc){
        TipoCitaResponseDTO responseDTO = new TipoCitaResponseDTO();
        responseDTO.setId(tc.getId());
        responseDTO.setNombre(tc.getNombre());
        return responseDTO;
    }
}
