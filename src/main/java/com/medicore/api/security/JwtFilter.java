package com.medicore.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    // Utilidad para operaciones sobre el token JWT (extracción de claims, validación)
    private final JwtUtil jwtUtil;

    // Servicio que carga los datos del usuario desde la base de datos
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Lógica principal del filtro. Spring Security lo invoca automáticamente
     * en cada petición entrante antes de que llegue al controlador.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // Leer el encabezado de autorización enviado por el cliente
        String header = request.getHeader("Authorization");

        // Si no hay encabezado o no tiene formato Bearer, continuar sin autenticar
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Extraer el token eliminando el prefijo "Bearer " (7 caracteres)
        String token = header.substring(7);

        try {
            // Obtener el nombre de usuario almacenado en el token (claim "sub")
            String username = jwtUtil.extractUsername(token);

            // Proceder solo si se obtuvo un username y el contexto aún no tiene autenticación
            // (evita sobrescribir una autenticación ya establecida en la misma petición)
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Cargar los detalles completos del usuario (roles, estado activo, etc.)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Verificar que el token sea válido: firma correcta y no expirado
                if (jwtUtil.isTokenValid(token, userDetails)) {
                    // Crear el objeto de autenticación con las autoridades del usuario
                    // Las credenciales se pasan como null porque ya fueron validadas mediante JWT
                    var auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // Adjuntar detalles adicionales de la petición (IP, session ID, etc.)
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Registrar la autenticación en el contexto de seguridad para esta petición
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ignored) {
            // Token inválido o expirado: se ignora la excepción y el SecurityContext
            // queda vacío, lo que provocará un 401 si el endpoint requiere autenticación
        }

        // Continuar con el siguiente filtro de la cadena
        chain.doFilter(request, response);
    }
}