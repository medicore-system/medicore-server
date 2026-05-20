package com.medicore.api.controllers;

import com.medicore.api.dtos.AuthResponseDTO;
import com.medicore.api.dtos.LoginRequestDTO;
import com.medicore.api.dtos.RegisterRequestDTO;
import com.medicore.api.services.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * Controlador encargado de gestionar las operaciones de autenticación del sistema.
 * Expone endpoints para registro y login de usuarios.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * Servicio de autenticación que contiene la lógica de negocio para registro y login.
     */
    private final IAuthService authService;

    /**
     * Endpoint encargado de registrar un nuevo usuario en el sistema.
     *
     * @param request objeto con la información necesaria para crear un nuevo usuario
     * @return ResponseEntity con el usuario autenticado y código HTTP 201 (CREATED)
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /**
     * Endpoint encargado de autenticar un usuario en el sistema.
     *
     * Si las credenciales son correctas retorna un token o información de autenticación.
     * Si ocurre un error de autenticación, captura la excepción ResponseStatusException
     * y retorna un mensaje controlado con el código HTTP correspondiente.
     *
     * En caso de error, se utiliza e.getReason() para obtener el mensaje definido
     * cuando se lanzó la excepción en la capa de servicio (por ejemplo:
     * "Credenciales incorrectas", "Usuario no encontrado", etc.).
     *
     * @param request objeto con las credenciales del usuario (email y contraseña)
     * @return ResponseEntity con la respuesta de autenticación o un mensaje de error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            AuthResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(Map.of("mensaje", e.getReason()));
        }
    }
}