package com.medicore.api.repositories.costos;

import com.medicore.api.entities.costos.Liquidacion;
import com.medicore.api.repositories.reportes.ReporteLiquidacionRepositoryCustom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiquidacionRepository extends JpaRepository<Liquidacion, String>, ReporteLiquidacionRepositoryCustom {
    
    List<Liquidacion> findByEpsCodigoOrderByFechaGeneracionDesc(String epsCodigo);
}