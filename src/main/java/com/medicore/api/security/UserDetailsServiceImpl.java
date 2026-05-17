package com.medicore.api.security;

import com.medicore.api.repositories.IUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Implementación de {@link UserDetailsService} de Spring Security.
 *
 * Se encarga de cargar los datos de un usuario desde la base de datos
 * durante el proceso de autenticación. Spring Security llama automáticamente
 * a {@code loadUserByUsername} cuando se intenta iniciar sesión.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repositorio para consultar usuarios en la base de datos
    private final IUsuarioRepository usuarioRepository;

    /**
     * Busca un usuario por su nombre de usuario y construye el objeto
     * {@link UserDetails} que Spring Security necesita para autenticar y autorizar.
     *
     * @param correo nombre de usuario recibido en el login
     * @return {@link UserDetails} con credenciales y roles del usuario
     * @throws UsernameNotFoundException si no existe un usuario con ese nombre
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        var usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + correo));

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));

        return new User(
                usuario.getCorreo(),
                usuario.getContrasena(),
                Boolean.TRUE.equals(usuario.getEstado()),
                true, true, true,
                authorities
        );
    }
}
