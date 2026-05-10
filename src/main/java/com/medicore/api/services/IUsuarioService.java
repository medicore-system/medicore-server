package com.medicore.api.services;

import com.medicore.api.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findByDocumento(String document);
    Usuario save(Usuario usuario);
    Optional<Usuario> update(String documento, Usuario usuario);
    Optional<Usuario> inhabilitarUsuario(String document);
}
