package com.medicore.api.repositories;

import com.medicore.api.entities.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio encargado de gestionar las operaciones
 * de acceso a datos para la entidad Medico.
 *
 * <p>
 * Extiende JpaRepository para proporcionar operaciones
 * CRUD y consultas básicas sobre la tabla de médicos.
 * </p>
 */
@Repository
public interface IMedicoRepository extends JpaRepository<Medico, String> {

}