package com.medicore.api.services;

import com.medicore.api.dtos.AuthResponseDTO;
import com.medicore.api.dtos.LoginRequestDTO;
import com.medicore.api.dtos.RegisterRequestDTO;

/**
 * Interfaz de servicio que define las operaciones de autenticación en MediCore.
 * Gestiona el registro de nuevos usuarios y el inicio de sesión con JWT.
 */
public interface IAuthService {

    /**
     * Registra un nuevo usuario en el sistema y retorna un token JWT.
     *
     * @param request datos del usuario a registrar
     * @return respuesta con el token JWT generado
     */
    AuthResponseDTO register(RegisterRequestDTO request);

    /**
     * Autentica un usuario con sus credenciales y retorna un token JWT.
     *
     * @param request credenciales de inicio de sesión
     * @return respuesta con el token JWT generado
     */
    AuthResponseDTO login(LoginRequestDTO request);
}
