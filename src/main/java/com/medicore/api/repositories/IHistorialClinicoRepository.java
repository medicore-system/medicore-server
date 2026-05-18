package com.medicore.api.repositories;

import com.medicore.api.entities.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad {@link HistorialClinico}.
 *
 * <p>
 * Se utiliza para validar y consultar historiales clínicos asociados
 * opcionalmente a los servicios médicos.
 * </p>
 */
@Repository
public interface IHistorialClinicoRepository extends JpaRepository<HistorialClinico, String> {
}