package com.medicore.api.repositories;

import com.medicore.api.entities.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICiudadRepository extends JpaRepository<Ciudad, String> {

    List<Ciudad> findByEstadoTrue();

    List<Ciudad> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndDepartamentoId(String nombre, Integer idDepartamento);

    boolean existsByNombreIgnoreCaseAndDepartamentoIdAndCodigoNot(
            String nombre, Integer idDepartamento, String codigo);

    @Query(value = """
            SELECT COALESCE(MAX(CAST(SUBSTRING(codigo FROM 5) AS INTEGER)), 0)
            FROM ciudad
            WHERE codigo ~ '^COL-[0-9]+$'
            """, nativeQuery = true)
    int findMaxCodigoSequence();
}
