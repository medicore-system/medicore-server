package com.medicore.api.repositories;

import com.medicore.api.entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, String> {
    
    // Encuentra facturas de una EPS en un rango de fechas que AÚN NO tienen liquidación
    List<Factura> findByEpsCodigoAndFechaBetweenAndLiquidacionIsNull(
            String epsCodigo, 
            LocalDate fechaInicio, 
            LocalDate fechaFin
    );
}