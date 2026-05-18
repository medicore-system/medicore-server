package com.medicore.api.controllers;

import com.medicore.api.dtos.servicio.ServicioDetalleResponse;
import com.medicore.api.dtos.servicio.ServicioRequest;
import com.medicore.api.dtos.servicio.ServicioResponse;
import com.medicore.api.dtos.servicio.TipoServicioResponse;
import com.medicore.api.services.IServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de exponer los endpoints de servicios médicos.
 *
 * <p>
 * Este controlador recibe las peticiones HTTP relacionadas con servicios
 * y delega la lógica de negocio a {@link IServicioService}.
 * </p>
 */
@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServicioController {

    private final IServicioService servicioService;

    /**
     * Lista o busca servicios médicos.
     *
     * <p>
     * Si se envía el parámetro {@code search}, se filtran los servicios.
     * Si no se envía, se retornan todos.
     * </p>
     *
     * @param search término opcional de búsqueda.
     * @return lista de servicios.
     */
    @GetMapping
    public ResponseEntity<List<ServicioResponse>> getServices(
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(servicioService.buscarServicios(search));
    }

    /**
     * Crea un nuevo servicio médico.
     *
     * @param request datos del servicio a crear.
     * @return servicio creado con estado HTTP 201.
     */
    @PostMapping
    public ResponseEntity<ServicioResponse> createService(
            @Valid @RequestBody ServicioRequest request
    ) {
        ServicioResponse response = servicioService.crearServicio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene el detalle completo de un servicio médico.
     *
     * @param id código del servicio.
     * @return detalle del servicio.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDetalleResponse> getServiceById(
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(servicioService.obtenerDetalleServicio(id));
    }

    /**
     * Actualiza un servicio médico existente.
     *
     * @param id código del servicio.
     * @param request datos actualizados.
     * @return servicio actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponse> updateService(
            @PathVariable("id") String id,
            @Valid @RequestBody ServicioRequest request
    ) {
        return ResponseEntity.ok(servicioService.editarServicio(id, request));
    }

    /**
     * Inactiva un servicio médico.
     *
     * @param id código del servicio.
     * @return respuesta sin contenido si la operación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
            @PathVariable("id") String id
    ) {
        servicioService.inactivarServicio(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista los tipos de servicio disponibles.
     *
     * @return lista de tipos de servicio.
     */
    @GetMapping("/types")
    public ResponseEntity<List<TipoServicioResponse>> getServiceTypes() {
        return ResponseEntity.ok(servicioService.listarTiposServicio());
    }
}