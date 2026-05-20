package com.medicore.api.repositories;

import com.medicore.api.entities.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para gestionar entidades {@link Departamento} en la base de datos.
 */
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {

    /**
     * Busca un departamento por nombre ignorando mayúsculas/minúsculas.
     *
     * @param nombre nombre del departamento a buscar
     * @return el departamento encontrado, o vacío si no existe
     */
    Optional<Departamento> findByNombreIgnoreCase(String nombre);
}
