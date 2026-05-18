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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder contrasenaEncoder;
    private final ICiudadRepository ciudadRepository;
    private final IEpsRepository epsRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.findByDocumento(request.getDocumento()).isPresent()) {
            throw new IllegalArgumentException("Username already taken: " + request.getDocumento());
        }
        System.out.println("CIUDAD ID = " + request.getCodigoCiudad());
        System.out.println("EPS ID = " + request.getCodigoEps());
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
        Eps e = epsRepository.findById(request.getCodigoEps())
                .orElseThrow(() -> new RuntimeException("Eps no encontrada"));
        usuario.setEps(e);
        usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());
        return buildResponse(usuario, userDetails, false);
    }



    public AuthResponseDTO login(LoginRequestDTO request) {
        System.out.println("DTO CORREO: " + request.getCorreo());
        System.out.println("DTO PASS: " + request.getContrasena());
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow();

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());

        return buildResponse(usuario, userDetails, true);
    }


    private AuthResponseDTO buildResponse(Usuario usuario, UserDetails userDetails, boolean bandera) {
        AuthResponseDTO response = new AuthResponseDTO();
        if(bandera){
            String token = jwtUtil.generateToken(userDetails);
            response.setToken(token);
        }
        response.setCorreo(usuario.getCorreo());
        response.setRole(usuario.getRol());

        return response;
    }
}
