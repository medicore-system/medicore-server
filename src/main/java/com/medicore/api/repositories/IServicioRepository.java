package com.medicore.api.repositories;

import com.medicore.api.entities.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IServicioRepository extends JpaRepository<Servicio, String> {

    List<Servicio> findAllByOrderByCodigoAsc();

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndCodigoNot(String nombre, String codigo);

    List<Servicio> findByCodigoContainingIgnoreCaseOrNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrderByCodigoAsc(
            String codigo,
            String nombre,
            String descripcion
    );

    List<Servicio> findByCodigoStartingWith(String prefijoConGuion);
}