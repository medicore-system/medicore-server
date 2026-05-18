package com.medicore.api.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuración central de Spring Security.
 *
 * Define la cadena de filtros HTTP, la política de sesiones, las reglas de
 * autorización por ruta, el manejo de errores de autenticación/autorización
 * y los beans necesarios para que el mecanismo JWT funcione.
 */
@Configuration
@EnableMethodSecurity   // habilita @PreAuthorize / @PostAuthorize en los controladores
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro que intercepta cada petición para validar el token JWT
    private final JwtFilter jwtFilter;

    // Implementación que carga los datos del usuario desde la base de datos
    private final UserDetailsService userDetailsService;

    /**
     * Cadena principal de filtros de seguridad HTTP.
     *
     * <ul>
     *   <li>Deshabilita CSRF porque la API es stateless (usa JWT, no cookies de sesión).</li>
     *   <li>Configura la sesión como STATELESS: no se crea ni almacena sesión en el servidor.</li>
     *   <li>Permite todas las rutas públicamente; el control fino se hace con @PreAuthorize.</li>
     *   <li>Devuelve 401 si el token es inválido/ausente y 403 si el rol no tiene permiso.</li>
     *   <li>Inserta {@link JwtFilter} antes del filtro estándar de usuario/contraseña.</li>
     * </ul>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Sin CSRF: las APIs REST stateless no necesitan protección CSRF
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // No se guardan sesiones; cada petición debe traer su propio JWT
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas de registro e inicio de sesión accesibles sin token
                        .requestMatchers("/auth/**").permitAll()
                        // El resto de endpoints son públicos aquí; se protegen con @PreAuthorize en cada controlador
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        // Token ausente o inválido → 401 Unauthorized
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        // Token válido pero sin el rol necesario → 403 Forbidden
                        .accessDeniedHandler((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_FORBIDDEN))
                )
                // Registra el proveedor que valida usuario y contraseña contra la BD
                .authenticationProvider(authenticationProvider())
                // El filtro JWT se ejecuta antes que el filtro de autenticación por formulario
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Proveedor de autenticación basado en base de datos (DAO).
     *
     * Combina {@link UserDetailsService} (para cargar el usuario) con
     * {@link PasswordEncoder} (para verificar la contraseña hasheada).
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Expone el {@link AuthenticationManager} como bean para poder inyectarlo
     * en el servicio de autenticación (login) y disparar la validación de credenciales.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Encoder de contraseñas usando BCrypt.
     *
     * BCrypt aplica un salt aleatorio y múltiples rondas de hashing,
     * lo que lo hace resistente a ataques de fuerza bruta y rainbow tables.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:4200"); // Angular
        config.addAllowedMethod("*"); // GET, POST, PUT, DELETE...
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}