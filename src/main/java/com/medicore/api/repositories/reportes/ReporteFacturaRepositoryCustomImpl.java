package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.entities.Factura;
import com.medicore.api.entities.hospital.Hospital;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReporteFacturaRepositoryCustomImpl implements ReporteFacturaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<IngresosHospitalDTO> obtenerIngresosPorHospitalYAnio(int anio) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<IngresosHospitalDTO> query = cb.createQuery(IngresosHospitalDTO.class);
        Root<Factura> factura = query.from(Factura.class);
        
        Join<Factura, Hospital> hospital = factura.join("hospital");

        // 1. Calculamos el rango de fechas en Java (POO Puro y elegante)
        // NOTA: Si tu entidad Factura usa LocalDateTime en lugar de LocalDate,
        // simplemente cambia esto por: LocalDateTime.of(anio, 1, 1, 0, 0)
        LocalDate inicioAnio = LocalDate.of(anio, 1, 1);
        LocalDate finAnio = LocalDate.of(anio, 12, 31);

        // 2. Proyección directa al Record
        query.select(cb.construct(
                IngresosHospitalDTO.class,
                hospital.get("codigo"),
                hospital.get("nombre"),
                cb.count(factura),
                cb.sum(factura.get("costoTotal"))
        ));

        // 3. Filtro SARGable: Usamos BETWEEN en lugar de extraer el año
        // Esto permite que la BD use índices y es agnóstico al motor SQL
        query.where(cb.between(factura.get("fecha"), inicioAnio, finAnio));

        // 4. Agrupación y Ordenamiento
        query.groupBy(hospital.get("codigo"), hospital.get("nombre"));
        query.orderBy(cb.desc(cb.sum(factura.get("costoTotal"))));

        return entityManager.createQuery(query).getResultList();
    }
}