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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link AuthServiceImpl}.
 *
 * <p>Verifica el comportamiento de las operaciones de registro
 * y autenticación, incluyendo:
 * <ul>
 *   <li>Rechazo de documentos duplicados al registrarse.</li>
 *   <li>Validación de existencia de ciudad y EPS.</li>
 *   <li>Manejo de credenciales inválidas en el login.</li>
 *   <li>Construcción de la respuesta con token JWT.</li>
 * </ul></p>
 *
 * <p>Aplica el patrón AAA (Arrange, Act, Assert) y el principio
 * de Single Responsibility: cada prueba valida un solo
 * comportamiento del servicio.</p>
 */
class AuthServiceImplTest {

    /**
     * Repositorio simulado de usuarios.
     */
    @Mock
    private IUsuarioRepository usuarioRepository;

    /**
     * Encoder simulado para encriptación de contraseñas.
     */
    @Mock
    private PasswordEncoder contrasenaEncoder;

    /**
     * Repositorio simulado de ciudades.
     */
    @Mock
    private ICiudadRepository ciudadRepository;

    /**
     * Repositorio simulado de EPS.
     */
    @Mock
    private IEpsRepository epsRepository;

    /**
     * Servicio simulado de carga de detalles de usuario para Spring Security.
     */
    @Mock
    private UserDetailsService userDetailsService;

    /**
     * Utilidad simulada para generación de tokens JWT.
     */
    @Mock
    private JwtUtil jwtUtil;

    /**
     * Manager simulado de autenticación de Spring Security.
     */
    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * Instancia del servicio bajo prueba con los mocks inyectados.
     */
    @InjectMocks
    private AuthServiceImpl authService;

    /**
     * Inicializa los mocks antes de cada prueba para garantizar
     * el aislamiento entre tests.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verifica que el método register lance {@link IllegalArgumentException}
     * cuando se intenta registrar un usuario con un documento que ya existe
     * en la base de datos.
     */
    @Test
    void registerDebeLanzarExcepcionSiElDocumentoYaExiste() {
        // --- Patrón AAA ---

        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setDocumento("123456789");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setDocumento("123456789");

        when(usuarioRepository.findByDocumento("123456789"))
                .thenReturn(Optional.of(usuarioExistente));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(request));

        assertTrue(exception.getMessage().contains("Username already taken"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Verifica que el método register lance una excepción cuando la ciudad
     * indicada en el request no existe en la base de datos.
     */
    @Test
    void registerDebeLanzarExcepcionSiLaCiudadNoExiste() {
        // --- Patrón AAA ---

        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setDocumento("123456789");
        request.setCorreo("test@test.com");
        request.setContrasena("password");
        request.setCodigoCiudad("CIU_INEXISTENTE");

        when(usuarioRepository.findByDocumento("123456789")).thenReturn(Optional.empty());
        when(contrasenaEncoder.encode("password")).thenReturn("encoded_password");
        when(ciudadRepository.findById("CIU_INEXISTENTE")).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(request));

        assertTrue(exception.getMessage().contains("Ciudad no encontrada"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Verifica que el método register lance una excepción cuando la EPS
     * indicada en el request no existe en la base de datos.
     */
    @Test
    void registerDebeLanzarExcepcionSiLaEpsNoExiste() {
        // --- Patrón AAA ---

        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setDocumento("123456789");
        request.setCorreo("test@test.com");
        request.setContrasena("password");
        request.setCodigoCiudad("CIU001");
        request.setCodigoEPS("EPS_INEXISTENTE");

        Ciudad ciudad = new Ciudad();
        ciudad.setCodigo("CIU001");

        when(usuarioRepository.findByDocumento("123456789")).thenReturn(Optional.empty());
        when(contrasenaEncoder.encode("password")).thenReturn("encoded_password");
        when(ciudadRepository.findById("CIU001")).thenReturn(Optional.of(ciudad));
        when(epsRepository.findById("EPS_INEXISTENTE")).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(request));

        assertTrue(exception.getMessage().contains("Eps no encontrada"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Verifica que un registro exitoso construya una respuesta válida
     * con token JWT, correo y documento del usuario.
     */
    @Test
    void registerDebeRetornarRespuestaConTokenCuandoLosDatosSonValidos() {
        // --- Patrón AAA ---

        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setDocumento("123456789");
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setCorreo("juan@test.com");
        request.setContrasena("password");
        request.setTelefono("3001234567");
        request.setCodigoCiudad("CIU001");
        request.setCodigoEPS("EPS001");

        Ciudad ciudad = new Ciudad();
        ciudad.setCodigo("CIU001");

        Eps eps = new Eps();
        eps.setCodigo("EPS001");

        UserDetails userDetails = new User(
                "juan@test.com", "encoded_password", Collections.emptyList());

        when(usuarioRepository.findByDocumento("123456789")).thenReturn(Optional.empty());
        when(contrasenaEncoder.encode("password")).thenReturn("encoded_password");
        when(ciudadRepository.findById("CIU001")).thenReturn(Optional.of(ciudad));
        when(epsRepository.findById("EPS001")).thenReturn(Optional.of(eps));
        when(userDetailsService.loadUserByUsername("juan@test.com")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("token_jwt_simulado");

        // Act
        AuthResponseDTO resultado = authService.register(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("token_jwt_simulado", resultado.getToken());
        assertEquals("juan@test.com", resultado.getCorreo());
        assertEquals("123456789", resultado.getDocumento());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    /**
     * Verifica que el método login lance {@link ResponseStatusException}
     * con código HTTP 401 cuando las credenciales son inválidas.
     */
    @Test
    void loginDebeLanzarExcepcionConCredencialesInvalidas() {
        // --- Patrón AAA ---

        // Arrange
        LoginRequestDTO request = new LoginRequestDTO();
        request.setCorreo("usuario@test.com");
        request.setContrasena("contrasena_incorrecta");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(request));

        assertEquals(401, exception.getStatusCode().value());
        assertTrue(exception.getReason().contains("contraseña incorrecta"));
        verify(jwtUtil, never()).generateToken(any());
    }
}
