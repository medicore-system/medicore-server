package com.medicore.api.repositories;

import com.medicore.api.entities.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * Busca médicos activos por especialidad.
     *
     * <p>Usado para filtrar los médicos disponibles
     * al momento de agendar una cita.</p>
     *
     * @param especialidadId ID de la especialidad.
     * @return lista de médicos activos con esa especialidad.
     */
    List<Medico> findByEspecialidadIdAndEstadoTrue(Integer especialidadId);

}