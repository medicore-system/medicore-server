package com.medicore.api.repositories.cita;

import com.medicore.api.entities.Cita.TipoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para gestionar entidades {@link TipoCita} en la base de datos.
 */
@Repository
public interface ITipoCitaRepository extends JpaRepository<TipoCita, Integer> {

}
