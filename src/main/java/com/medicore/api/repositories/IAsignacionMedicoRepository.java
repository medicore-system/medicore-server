package com.medicore.api.repositories;

import com.medicore.api.entities.AsignacionMedico;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
