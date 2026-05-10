package com.medicore.api.controllers;

import com.medicore.api.dtos.Usuario.UsuarioCreateRequestDTO;
import com.medicore.api.dtos.Usuario.UsuarioResponseDTO;
import com.medicore.api.dtos.Usuario.UsuarioUpdateRequestDTO;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Usuario;
import com.medicore.api.repositories.CiudadRepository;
import com.medicore.api.repositories.IEpsRepository;
import com.medicore.api.services.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final CiudadRepository ciudadRepository;
    private final IEpsRepository epsRepository;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> findAll(){
        List<UsuarioResponseDTO> response = usuarioService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{documento}")
    public ResponseEntity<UsuarioResponseDTO> findByDocumento(@PathVariable String documento){
        return usuarioService.findByDocumento(documento)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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
