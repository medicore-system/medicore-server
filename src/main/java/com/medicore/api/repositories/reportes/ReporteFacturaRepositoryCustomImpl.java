package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.entities.Factura;
import com.medicore.api.entities.hospital.Hospital;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import java.util.List;

public class ReporteFacturaRepositoryCustomImpl implements ReporteFacturaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<IngresosHospitalDTO> obtenerIngresosPorHospitalYAnio(int anio) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<IngresosHospitalDTO> query = cb.createQuery(IngresosHospitalDTO.class);
        Root<Factura> factura = query.from(Factura.class);
        
        // Hacemos el Join con Hospital para obtener los datos necesarios
        Join<Factura, Hospital> hospital = factura.join("hospital");

        // Expresión para extraer el año de la fecha de la factura
        Expression<Integer> anioExpression = cb.function("EXTRACT", Integer.class, 
                cb.literal("YEAR"), factura.get("fecha"));

        // Definimos la proyección directa al RECORD (Constructor Expression de forma orientada a objetos)
        query.select(cb.construct(
                IngresosHospitalDTO.class,
                hospital.get("codigo"),
                hospital.get("nombre"),
                cb.count(factura),
                cb.sum(factura.get("costoTotal"))
        ));

        // Filtro: WHERE YEAR(fecha) = :anio
        query.where(cb.equal(anioExpression, anio));

        // Agrupación: GROUP BY h.codigo, h.nombre
        query.groupBy(hospital.get("codigo"), hospital.get("nombre"));

        // Ordenamiento: ORDER BY SUM(costoTotal) DESC
        query.orderBy(cb.desc(cb.sum(factura.get("costoTotal"))));

        return entityManager.createQuery(query).getResultList();
    }
}