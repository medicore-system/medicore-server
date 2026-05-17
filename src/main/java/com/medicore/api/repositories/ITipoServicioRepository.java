package com.medicore.api.repositories;

import com.medicore.api.entities.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITipoServicioRepository extends JpaRepository<TipoServicio, Integer> {

    List<TipoServicio> findAllByOrderByNombreAsc();
}