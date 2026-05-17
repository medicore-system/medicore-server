package com.medicore.api.repositories;

import com.medicore.api.entities.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio encargado de gestionar las operaciones
 * de acceso a datos para la entidad Especialidad.
 *
 * <p>
 * Extiende JpaRepository para proporcionar operaciones
 * CRUD y consultas básicas sobre la tabla de especialidades.
 * </p>
 */
@Repository
public interface IEspecialidadRepository extends JpaRepository<Especialidad, Integer> {
}