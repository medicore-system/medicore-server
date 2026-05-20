package com.medicore.api.repositories;

import com.medicore.api.entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.medicore.api.repositories.reportes.ReporteFacturaRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para gestionar entidades {@link Factura} en la base de datos.
 * Extiende {@link ReporteFacturaRepositoryCustom} para consultas de reportes personalizadas.
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, String>, ReporteFacturaRepositoryCustom {

/**
     * Busca facturas de una EPS en un rango de fechas que aún no han sido incluidas en una liquidación.
     *
     * @param epsCodigo    código de la EPS
     * @param fechaInicio  fecha de inicio del rango
     * @param fechaFin     fecha de fin del rango
     * @return lista de facturas sin liquidar para la EPS en el período indicado
     */
    List<Factura> findByEpsCodigoAndFechaBetweenAndLiquidacionIsNull(
            String epsCodigo,
            LocalDate fechaInicio,
            LocalDate fechaFin
    )
    @Query("SELECT f FROM Factura f WHERE f.cita.usuario.documento = :documentoPaciente AND f.pacientePago = false")
    List<Factura> findFacturasPendientesCaja(@Param("documentoPaciente") String documentoPaciente);
}