package com.medicore.api.repositories;

import com.medicore.api.entities.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link TipoServicio}.
 *
 * <p>
 * Administra la consulta de los tipos de servicio disponibles en el sistema,
 * tales como consulta, examen o procedimiento.
 * </p>
 */
@Repository
public interface ITipoServicioRepository extends JpaRepository<TipoServicio, Integer> {

    /**
     * Obtiene todos los tipos de servicio ordenados alfabéticamente por nombre.
     *
     * @return lista de tipos de servicio ordenados por nombre.
     */
    List<TipoServicio> findAllByOrderByNombreAsc();
}