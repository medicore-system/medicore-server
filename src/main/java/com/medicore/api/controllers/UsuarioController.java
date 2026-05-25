package com.medicore.api.controllers;

import com.medicore.api.dtos.Usuario.UsuarioCreateRequestDTO;
import com.medicore.api.dtos.Usuario.UsuarioResponseDTO;
import com.medicore.api.dtos.Usuario.UsuarioUpdateRequestDTO;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Usuario;
import com.medicore.api.repositories.ICiudadRepository;
import com.medicore.api.repositories.IEpsRepository;
import com.medicore.api.services.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con los usuarios del sistema.
 *
 * <p>Permite consultar, registrar, actualizar e
 * inhabilitar usuarios.</p>
 *
 * Base URL: /Usuarios
 *
 * @author Manuel
 */
@RestController
@RequestMapping("/Usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    /**
     * Servicio encargado de la lógica de negocio de usuarios.
     */
    private final IUsuarioService usuarioService;

    /**
     * Repositorio para la gestión de ciudades.
     */
    private final ICiudadRepository ciudadRepository;

    /**
     * Repositorio para la gestión de EPS.
     */
    private final IEpsRepository epsRepository;

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios en formato DTO.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> findAll(){
        List<UsuarioResponseDTO> response = usuarioService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Busca un usuario por su documento.
     *
     * @param documento documento del usuario.
     * @return usuario encontrado o respuesta 404 si no existe.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{documento}")
    public ResponseEntity<UsuarioResponseDTO> findByDocumento(@PathVariable String documento){
        return usuarioService.findByDocumento(documento)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>El método valida la existencia de:
     * <ul>
     *     <li>La ciudad asociada.</li>
     *     <li>La EPS asociada.</li>
     * </ul>
     * Luego crea y almacena el usuario.</p>
     *
     * @param request DTO con la información del usuario.
     * @return usuario creado con estado HTTP 201.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> save(@RequestBody UsuarioCreateRequestDTO request){

        Ciudad ciudad = ciudadRepository.findById(request.getCodigo_ciudad())
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
        Eps eps = epsRepository.findById(request.getCodigo_eps())
                .orElseThrow(() -> new RuntimeException("Eps no encontrada"));
        Usuario usuario = new Usuario();
        usuario.setDocumento(request.getDocumento());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setCorreo(request.getCorreo());
        usuario.setTelefono(request.getTelefono());
        usuario.setEps(eps);
        usuario.setCiudad(ciudad);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(usuarioService.save(usuario)));
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * <p>Permite modificar:
     * <ul>
     *     <li>Nombre</li>
     *     <li>Apellido</li>
     *     <li>Teléfono</li>
     *     <li>EPS</li>
     *     <li>Ciudad</li>
     * </ul>
     * </p>
     *
     * @param documento documento del usuario a actualizar.
     * @param request DTO con la nueva información.
     * @return usuario actualizado o 404 si no existe.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{documento}")
    public ResponseEntity<UsuarioResponseDTO> update(@PathVariable String documento, @RequestBody UsuarioUpdateRequestDTO request){
        Ciudad ciudad = ciudadRepository.findById(request.getCodigo_ciudad())
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
        Eps eps = epsRepository.findById(request.getCodigo_eps())
                .orElseThrow(() -> new RuntimeException("Eps no encontrada"));
        Usuario usuario = usuarioService.findByDocumento(documento)
                .orElseThrow(() -> new RuntimeException("Documento no encontrada"));
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());
        usuario.setEps(eps);
        usuario.setCiudad(ciudad);

        return usuarioService.update(documento, usuario)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cambia el estado de un usuario.
     *
     * <p>Si el usuario está habilitado, se inhabilita.
     * Si está inhabilitado, se habilita nuevamente.</p>
     *
     * @param documento documento del usuario.
     * @return usuario con el estado actualizado o 404 si no existe.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/inhabilitar/{documento}")
    public ResponseEntity<UsuarioResponseDTO> inhabilitarUsuario(@PathVariable String documento){
        return usuarioService.findByDocumento(documento)
                .map(existing ->{
                    existing.setEstado(!existing.getEstado());
                    return toResponse(usuarioService.save(existing));
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Convierte una entidad {@link Usuario} en un DTO de respuesta.
     *
     * <p>Este método encapsula la transformación de entidades
     * hacia objetos de transferencia de datos.</p>
     *
     * @param usuario entidad usuario.
     * @return DTO de respuesta del usuario.
     */
    private UsuarioResponseDTO toResponse(Usuario usuario){
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setDocumento(usuario.getDocumento());
        responseDTO.setNombre(usuario.getNombre());
        responseDTO.setApellido(usuario.getApellido());
        responseDTO.setCorreo(usuario.getCorreo());
        if(usuario.getEps() != null){
            responseDTO.setEps(usuario.getEps().getNombre());
        }
        if(usuario.getCiudad() != null){
            responseDTO.setCiudad(usuario.getCiudad().getNombre());
        }
        responseDTO.setTelefono(usuario.getTelefono());
        responseDTO.setRol(usuario.getRol());
        responseDTO.setEstado(usuario.getEstado());
        return responseDTO;
    }
}
