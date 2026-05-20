package com.medicore.api.services.impl;

import com.medicore.api.dtos.AuthResponseDTO;
import com.medicore.api.dtos.LoginRequestDTO;
import com.medicore.api.dtos.RegisterRequestDTO;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Usuario;
import com.medicore.api.repositories.ICiudadRepository;
import com.medicore.api.repositories.IEpsRepository;
import com.medicore.api.repositories.IUsuarioRepository;
import com.medicore.api.security.JwtUtil;
import com.medicore.api.services.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Implementación del servicio de autenticación del sistema.
 *
 * Encargado de manejar el registro de usuarios, autenticación,
 * generación de tokens JWT y construcción de respuestas de login.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    /**
     * Repositorio de usuarios para persistencia y consultas en base de datos.
     */
    private final IUsuarioRepository usuarioRepository;

    /**
     * Encoder para encriptar contraseñas usando BCrypt.
     */
    private final PasswordEncoder contrasenaEncoder;

    /**
     * Repositorio de ciudades para asociar el usuario a su ubicación.
     */
    private final ICiudadRepository ciudadRepository;

    /**
     * Repositorio de EPS para asociar el usuario a su entidad de salud.
     */
    private final IEpsRepository epsRepository;

    /**
     * Servicio de Spring Security para cargar usuarios autenticables.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Utilidad encargada de generar y validar tokens JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     * Manager de autenticación de Spring Security que valida credenciales.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Valida que el documento no esté registrado previamente,
     * encripta la contraseña, asigna ciudad y EPS, y guarda el usuario.
     *
     * Finalmente genera un token JWT para el usuario registrado.
     *
     * @param request datos necesarios para registrar un usuario
     * @return respuesta de autenticación con token y datos del usuario
     * @throws IllegalArgumentException si el documento ya está registrado
     */
    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (usuarioRepository.findByDocumento(request.getDocumento()).isPresent()) {
            throw new IllegalArgumentException("Username already taken: " + request.getDocumento());
        }

        Usuario usuario = new Usuario();
        usuario.setDocumento(request.getDocumento());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(contrasenaEncoder.encode(request.getContrasena()));
        usuario.setTelefono(request.getTelefono());

        Ciudad ciudad = ciudadRepository.findById(request.getCodigoCiudad())
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
        usuario.setCiudad(ciudad);

        Eps e = epsRepository.findById(request.getCodigoEPS())
                .orElseThrow(() -> new RuntimeException("Eps no encontrada"));
        usuario.setEps(e);

        usuario.setEstado(true);
        usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());
        return buildResponse(usuario, userDetails);
    }

    /**
     * Autentica un usuario en el sistema.
     *
     * Utiliza el AuthenticationManager de Spring Security para validar
     * las credenciales. Si son incorrectas, lanza una excepción HTTP 401.
     *
     * Si la autenticación es exitosa, genera un token JWT y retorna
     * la información del usuario autenticado.
     *
     * @param request credenciales de login (correo y contraseña)
     * @return respuesta con token JWT y datos del usuario
     * @throws ResponseStatusException si las credenciales son inválidas
     */
    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {

        try {
            /* SPRING SECURITY VALIDA TODO */
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(),
                            request.getContrasena()
                    )
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario no encontrado o contraseña incorrecta"
            );
        }

        /* SI LLEGA AQUI, LAS CREDENCIALES SON CORRECTAS */
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow();

        return buildResponse(usuario, userDetails);
    }

    /**
     * Construye la respuesta de autenticación del sistema.
     *
     * Genera el token JWT y agrega la información básica del usuario.
     *
     * @param usuario entidad usuario desde la base de datos
     * @param userDetails detalles de Spring Security del usuario autenticado
     * @return objeto AuthResponseDTO con token y datos del usuario
     */
    private AuthResponseDTO buildResponse(Usuario usuario, UserDetails userDetails) {

        String token = jwtUtil.generateToken(userDetails);

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setCorreo(usuario.getCorreo());
        response.setRole(usuario.getRol());

        return response;
    }
}