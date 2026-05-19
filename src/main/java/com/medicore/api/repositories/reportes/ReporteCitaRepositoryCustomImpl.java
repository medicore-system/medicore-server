package com.medicore.api.repositories.reportes;

import com.medicore.api.dtos.reportes.ProductividadMedicoDTO;
import com.medicore.api.entities.Cita;
import com.medicore.api.entities.Medico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public class ReporteCitaRepositoryCustomImpl implements ReporteCitaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductividadMedicoDTO> obtenerProductividadMedicaPorMes(int anio, int mes) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductividadMedicoDTO> query = cb.createQuery(ProductividadMedicoDTO.class);
        Root<Cita> cita = query.from(Cita.class);

        Join<Cita, Medico> medico = cita.join("medico");

        YearMonth anioMes = YearMonth.of(anio, mes);
        LocalDateTime inicioMes = anioMes.atDay(1).atStartOfDay();
        LocalDateTime finMes = anioMes.atEndOfMonth().atTime(23, 59, 59);

        query.select(cb.construct(
                ProductividadMedicoDTO.class,
                medico.get("documento"),
                medico.get("nombre"),
                medico.get("apellido"),
                cb.count(cita)
        ));

        query.where(
                cb.and(
                        cb.between(cita.get("fecha"), inicioMes, finMes),
                        cb.equal(cita.get("estado"), "COMPLETADA")
                )
        );

        query.groupBy(medico.get("documento"), medico.get("nombre"), medico.get("apellido"));
        query.orderBy(cb.desc(cb.count(cita)));

        return entityManager.createQuery(query).getResultList();
    }
}