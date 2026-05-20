package com.medicore.api.repositories;

import com.medicore.api.entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Factura}.
 *
 * <p>
 * Permite consultar facturas asociadas a un servicio médico sin utilizar
 * consultas SQL quemadas. La navegación se realiza a través de la relación
 * entre {@code Factura} y {@code Servicio}.
 * </p>
 */
@Repository
public interface IFacturaRepository extends JpaRepository<Factura, String> {

    /**
     * Busca las facturas asociadas a un servicio específico.
     *
     * <p>
     * Spring Data JPA interpreta el nombre del método como una consulta sobre
     * la relación {@code factura.servicio.codigo}.
     * </p>
     *
     * @param codigoServicio código del servicio médico.
     * @return lista de facturas asociadas al servicio, ordenadas por fecha descendente y código ascendente.
     */
    List<Factura> findByServicioCodigoOrderByFechaDescCodigoAsc(String codigoServicio);
}