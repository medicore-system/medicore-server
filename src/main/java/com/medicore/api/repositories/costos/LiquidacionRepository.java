package com.medicore.api.repositories.costos;

import com.medicore.api.entities.costos.Liquidacion;
import com.medicore.api.repositories.reportes.ReporteLiquidacionRepositoryCustom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para gestionar entidades {@link Liquidacion} en la base de datos.
 * Extiende {@link ReporteLiquidacionRepositoryCustom} para consultas de reportes personalizadas.
 */
@Repository
public interface LiquidacionRepository extends JpaRepository<Liquidacion, String>, ReporteLiquidacionRepositoryCustom {

    /**
     * Obtiene todas las liquidaciones de una EPS ordenadas por fecha de generación descendente.
     *
     * @param epsCodigo código de la EPS
     * @return lista de liquidaciones de la EPS, más recientes primero
     */
    List<Liquidacion> findByEpsCodigoOrderByFechaGeneracionDesc(String epsCodigo);
}