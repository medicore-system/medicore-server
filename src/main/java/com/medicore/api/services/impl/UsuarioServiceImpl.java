package com.medicore.api.services.impl;

import com.medicore.api.entities.Usuario;
import com.medicore.api.repositories.IUsuarioRepository;
import com.medicore.api.services.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findByDocumento(String document) {
        return usuarioRepository.findByDocumento(document);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> update(String documento, Usuario usuario) {
        return usuarioRepository.findByDocumento(documento).map(existing ->{
           existing.setNombre(usuario.getNombre());
           existing.setApellido(usuario.getApellido());
           existing.setCorreo(usuario.getCorreo());
           existing.setEps(usuario.getEps());
           existing.setCiudad(usuario.getCiudad());
           existing.setTelefono(usuario.getTelefono());
           return usuarioRepository.save(existing);
        });
    }

    @Override
    public Optional<Usuario> inhabilitarUsuario(String document) {
        return usuarioRepository.findByDocumento(document).map(existing ->{
            existing.setEstado(false);
            return usuarioRepository.save(existing);
        });
    }
}
