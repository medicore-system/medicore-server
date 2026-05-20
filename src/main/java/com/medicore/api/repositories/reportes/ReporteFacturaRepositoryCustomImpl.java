package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.entities.Factura;
import com.medicore.api.entities.hospital.Hospital;
import com.medicore.api.entities.Cita.Cita;
import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Especialidad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de {@link ReporteFacturaRepositoryCustom} que usa JPA Criteria API
 * para generar reportes financieros a partir de las facturas emitidas.
 */
public class ReporteFacturaRepositoryCustomImpl implements ReporteFacturaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     * Agrupa facturas por hospital y calcula el total de ingresos del año.
     */
    @Override
    public List<IngresosHospitalDTO> obtenerIngresosPorHospitalYAnio(int anio) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<IngresosHospitalDTO> query = cb.createQuery(IngresosHospitalDTO.class);
        Root<Factura> factura = query.from(Factura.class);

        Join<Factura, Hospital> hospital = factura.join("hospital");

        LocalDate inicioAnio = LocalDate.of(anio, 1, 1);
        LocalDate finAnio = LocalDate.of(anio, 12, 31);

        query.select(cb.construct(
                IngresosHospitalDTO.class,
                hospital.get("codigo"),
                hospital.get("nombre"),
                cb.count(factura),
                cb.sum(factura.get("costoTotal"))));

        query.where(cb.between(factura.get("fecha"), inicioAnio, finAnio));

        query.groupBy(hospital.get("codigo"), hospital.get("nombre"));
        query.orderBy(cb.desc(cb.sum(factura.get("costoTotal"))));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<IngresosEspecialidadDTO> obtenerIngresosPorEspecialidadYAnio(int anio) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<IngresosEspecialidadDTO> query = cb.createQuery(IngresosEspecialidadDTO.class);
        Root<Factura> factura = query.from(Factura.class);

        Join<Factura, Cita> cita = factura.join("cita");
        Join<Cita, Especialidad> especialidad = cita.join("especialidad");

        LocalDate inicioAnio = LocalDate.of(anio, 1, 1);
        LocalDate finAnio = LocalDate.of(anio, 12, 31);

        query.select(cb.construct(
                IngresosEspecialidadDTO.class,
                especialidad.get("id"),
                especialidad.get("nombre"),
                cb.count(factura),
                cb.sum(factura.get("costoTotal"))));

        query.where(cb.between(factura.get("fecha"), inicioAnio, finAnio));

        query.groupBy(especialidad.get("id"), especialidad.get("nombre"));
        query.orderBy(cb.desc(cb.sum(factura.get("costoTotal"))));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<AtencionesEpsDTO> obtenerAtencionesPorEpsYAnio(int anio) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AtencionesEpsDTO> query = cb.createQuery(AtencionesEpsDTO.class);
        Root<Factura> factura = query.from(Factura.class);

        Join<Factura, Eps> eps = factura.join("eps");

        LocalDate inicioAnio = LocalDate.of(anio, 1, 1);
        LocalDate finAnio = LocalDate.of(anio, 12, 31);

        query.select(cb.construct(
                AtencionesEpsDTO.class,
                eps.get("codigo"),
                eps.get("nombre"),
                cb.count(factura),
                cb.sum(factura.get("costoTotal"))
        ));

        query.where(cb.between(factura.get("fecha"), inicioAnio, finAnio));

        query.groupBy(eps.get("codigo"), eps.get("nombre"));

        query.orderBy(cb.desc(cb.count(factura)));

        return entityManager.createQuery(query).getResultList();
    }
}