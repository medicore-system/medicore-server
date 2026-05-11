package com.medicore.api.repositories;

import com.medicore.api.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio encargado de la persistencia y consulta
 * de entidades {@link Usuario}.
 *
 * <p>Extiende {@link JpaRepository} para proporcionar
 * operaciones CRUD y consultas personalizadas
 * relacionadas con usuarios.</p>
 *
 * @author Manuel
 */
@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, String> {

    /**
     * Busca un usuario por su documento de identificación.
     *
     * @param documento documento del usuario.
     * @return un Optional con el usuario encontrado,
     * o vacío si no existe.
     */
    Optional<Usuario> findByDocumento(String documento);
}
