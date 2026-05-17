package com.medicore.api.repositories;

import com.medicore.api.entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFacturaRepository extends JpaRepository<Factura, String> {

    List<Factura> findByServicioCodigoOrderByFechaDescCodigoAsc(String codigoServicio);
}