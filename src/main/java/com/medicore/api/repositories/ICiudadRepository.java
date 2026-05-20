package com.medicore.api.repositories;

import com.medicore.api.entities.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para gestionar entidades {@link Ciudad} en la base de datos.
 */
@Repository
public interface ICiudadRepository extends JpaRepository<Ciudad, String> {

    /**
     * Retorna todas las ciudades que están activas en el sistema.
     *
     * @return lista de ciudades activas
     */
    List<Ciudad> findByEstadoTrue();

    /**
     * Busca ciudades cuyo nombre contiene el texto indicado (sin distinción de mayúsculas).
     *
     * @param nombre texto a buscar en el nombre de la ciudad
     * @return lista de ciudades que coinciden
     */
    List<Ciudad> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Verifica si ya existe una ciudad con el mismo nombre en el mismo departamento.
     *
     * @param nombre         nombre de la ciudad
     * @param idDepartamento id del departamento
     * @return true si ya existe una ciudad con ese nombre en el departamento
     */
    boolean existsByNombreIgnoreCaseAndDepartamentoId(String nombre, Integer idDepartamento);

    /**
     * Verifica si existe una ciudad con el mismo nombre en el mismo departamento, excluyendo un código.
     *
     * @param nombre         nombre de la ciudad
     * @param idDepartamento id del departamento
     * @param codigo         código de la ciudad a excluir (usada en actualizaciones)
     * @return true si hay duplicado en otro registro
     */
    boolean existsByNombreIgnoreCaseAndDepartamentoIdAndCodigoNot(
            String nombre, Integer idDepartamento, String codigo);

    /**
     * Retorna el número secuencial máximo de los códigos de ciudad con formato "CIU-{n}".
     *
     * @return el mayor número de secuencia encontrado, o 0 si no existe ninguno
     */
    @Query(value = """
            SELECT COALESCE(MAX(CAST(SUBSTRING(codigo FROM 5) AS INTEGER)), 0)
            FROM ciudad
            WHERE codigo ~ '^CIU-[0-9]+$'
            """, nativeQuery = true)
    int findMaxCodigoSequence();
}
