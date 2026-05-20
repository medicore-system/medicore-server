package com.medicore.api.repositories;

import com.medicore.api.entities.AsignacionMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAsignacionMedicoRepository extends JpaRepository<AsignacionMedico,Integer> {
    /**
     * Calcula el ultimo turno del medico y con este calcula la
     * FechaInicio y el siguiente horario del medico
     *
     * @param documentoMedico documento unico de identificacion del medico
     */
    Optional<AsignacionMedico> findTopByMedicoDocumentoOrderByFechaFinDesc(String documentoMedico);

    /**
     * Calcula la ultima asignacion en la ciudad para hacer
     * una asignacion de medicos en formato round-robin de hospitales
     *
     * @param codigoCiudad
     */
    Optional<AsignacionMedico> findTopByCiudadCodigoOrderByCodigoDesc(String codigoCiudad);

    List<AsignacionMedico> findByMedicoDocumentoAndEstadoTrue(String documentoMedico);

    List<AsignacionMedico> findByHospitalCodigoAndEstadoTrue(String codigoHospital);

    List<AsignacionMedico> findByEstadoTrueAndFechaFinBefore(LocalDate fecha);

    /**
     * Médicos con asignación activa para una especialidad dada.
     * Usado en GET /disponibilidad/medicos?id_especialidad=X
     */
    @Query("SELECT a FROM AsignacionMedico a " +
            "WHERE a.medico.especialidad.id = :idEspecialidad " +
            "AND a.estado = true")
    List<AsignacionMedico> findActivasByEspecialidad(@Param("idEspecialidad") Integer idEspecialidad);

    /**
     * Asignación activa del médico que cubre una fecha específica.
     * Usado en GET /disponibilidad/horarios/{documento_medico}?fecha=X
     */
    @Query("SELECT a FROM AsignacionMedico a " +
            "WHERE a.medico.documento = :documento_medico " +
            "AND a.estado = true " +
            "AND a.fechaInicio <= :fecha " +
            "AND a.fechaFin >= :fecha")
    List<AsignacionMedico> findActivasByMedicoEnFecha(@Param("documento_medico") String documento_medico,
                                                      @Param("fecha") LocalDate fecha);
}
