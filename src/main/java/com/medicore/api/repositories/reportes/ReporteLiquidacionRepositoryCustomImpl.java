package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.EstadoCarteraDTO;
import com.medicore.api.entities.costos.Liquidacion;
import com.medicore.api.entities.Eps;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import java.util.List;

/**
 * Implementación de {@link ReporteLiquidacionRepositoryCustom} que usa JPA Criteria API
 * para calcular el estado de cartera pendiente agrupado por EPS.
 */
public class ReporteLiquidacionRepositoryCustomImpl implements ReporteLiquidacionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     * Suma el total de cobertura EPS de las liquidaciones en estado PENDIENTE, agrupadas por EPS.
     */
    @Override
    public List<EstadoCarteraDTO> obtenerEstadoCarteraPorEps() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EstadoCarteraDTO> query = cb.createQuery(EstadoCarteraDTO.class);
        Root<Liquidacion> liquidacion = query.from(Liquidacion.class);
        
        Join<Liquidacion, Eps> eps = liquidacion.join("eps");

        query.select(cb.construct(
                EstadoCarteraDTO.class,
                eps.get("codigo"),
                eps.get("nombre"),
                cb.count(liquidacion),
                cb.sum(liquidacion.get("totalCoberturaEps"))
        ));

        query.where(cb.equal(liquidacion.get("estado"), "PENDIENTE"));

        query.groupBy(eps.get("codigo"), eps.get("nombre"));
        query.orderBy(cb.desc(cb.sum(liquidacion.get("totalCoberturaEps"))));

        return entityManager.createQuery(query).getResultList();
    }
}