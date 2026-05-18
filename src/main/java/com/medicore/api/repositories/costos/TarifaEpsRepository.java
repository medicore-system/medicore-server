package com.medicore.api.repositories.costos;

import com.medicore.api.entities.costos.TarifaEps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarifaEpsRepository extends JpaRepository<TarifaEps, String> {
    
    // Busca todas las tarifas activas de una EPS en particular
    List<TarifaEps> findByEpsCodigoAndEstadoTrue(String epsCodigo);

    // Valida si ya existe una tarifa para esa EPS y ese Servicio
    boolean existsByEpsCodigoAndServicioCodigo(String epsCodigo, String servicioCodigo);
    
    // Busca una tarifa específica por EPS y Servicio
    Optional<TarifaEps> findByEpsCodigoAndServicioCodigo(String epsCodigo, String servicioCodigo);
}