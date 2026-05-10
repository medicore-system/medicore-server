package com.medicore.api.repositories;

import com.medicore.api.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByDocumento(String documento);
}
