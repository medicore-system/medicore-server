package com.medicore.api.repositories;

import com.medicore.api.entities.Eps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio encargado de la persistencia y consulta
 * de entidades {@link Eps}.
 *
 * <p>Extiende {@link JpaRepository} para proporcionar
 * operaciones CRUD y consultas personalizadas.</p>
 *
 * @author Manuel
 */
@Repository
public interface IEpsRepository extends JpaRepository<Eps, String> {
    /**
     * Busca una EPS por su nombre.
     *
     * @param nombre nombre de la EPS.
     * @return un Optional con la EPS encontrada,
     * o vacío si no existe.
     */
    Optional<Eps> findByNombre(String nombre);
}
