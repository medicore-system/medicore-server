package com.medicore.api.repositories;

import com.medicore.api.entities.TipoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio encargado de la persistencia y consulta
 * de entidades {@link TipoCita}.
 *
 * <p>Extiende {@link JpaRepository} para proporcionar
 * operaciones CRUD sobre los tipos de cita.</p>
 *
 * @author Manuel
 */
@Repository
public interface ITipoCitaRepository extends JpaRepository<TipoCita, Integer> {
}
