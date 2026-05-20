package com.medicore.api.repositories.costos;

import com.medicore.api.entities.costos.TarifaEps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para gestionar entidades {@link TarifaEps} en la base de datos.
 */
@Repository
public interface TarifaEpsRepository extends JpaRepository<TarifaEps, String> {

    /**
     * Retorna todas las tarifas activas asociadas a una EPS.
     *
     * @param epsCodigo código de la EPS
     * @return lista de tarifas activas de la EPS
     */
    List<TarifaEps> findByEpsCodigoAndEstadoTrue(String epsCodigo);

    /**
     * Verifica si ya existe una tarifa para la combinación de EPS y servicio.
     *
     * @param epsCodigo      código de la EPS
     * @param servicioCodigo código del servicio
     * @return true si ya existe la tarifa
     */
    boolean existsByEpsCodigoAndServicioCodigo(String epsCodigo, String servicioCodigo);

    /**
     * Busca la tarifa específica de un servicio para una EPS.
     *
     * @param epsCodigo      código de la EPS
     * @param servicioCodigo código del servicio
     * @return la tarifa encontrada, o vacío si no existe
     */
    Optional<TarifaEps> findByEpsCodigoAndServicioCodigo(String epsCodigo, String servicioCodigo);
}