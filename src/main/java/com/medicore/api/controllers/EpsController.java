package com.medicore.api.controllers;

import com.medicore.api.dtos.Usuario.UsuarioResponseDTO;
import com.medicore.api.dtos.eps.EpsResponseDTO;
import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Usuario;
import com.medicore.api.services.IEpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con las eps del sistema.
 *
 * <p>Permite consultar eps.</p>
 *
 * Base URL: /Eps
 *
 * @author Manuel
 */
@RestController
@RequestMapping("/Eps")
@RequiredArgsConstructor
public class EpsController {

    /**
     * Servicio encargado de la lógica de negocio de eps.
     */
    private final IEpsService epsService;

    /**
     * Obtiene todas las eps registradas.
     *
     * @return lista de eps en formato DTO.
     */
    @GetMapping
    public ResponseEntity<List<EpsResponseDTO>> findAll(){
        List<EpsResponseDTO> response = epsService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Busca una eps por su codigo.
     *
     * @param codigo codigo de la eps.
     * @return eps encontrada o respuesta 404 si no existe.
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<EpsResponseDTO> findById(@PathVariable String codigo){
        return epsService.findById(codigo)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Convierte una entidad {@link Eps} en un DTO de respuesta.
     *
     * <p>Este método encapsula la transformación de entidades
     * hacia objetos de transferencia de datos.</p>
     *
     * @param eps entidad Eps.
     * @return DTO de respuesta de la eps.
     */
    private EpsResponseDTO toResponse(Eps eps){
        EpsResponseDTO responseDTO = new EpsResponseDTO();
        responseDTO.setCodigo(eps.getCodigo());
        responseDTO.setNombre(eps.getNombre());
        return responseDTO;
    }
}
