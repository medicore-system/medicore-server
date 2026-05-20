package com.medicore.api.services;

import com.medicore.api.entities.AsignacionMedico;
import com.medicore.api.entities.HorarioMedico;
import com.medicore.api.entities.hospital.Hospital;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio que define las operaciones de gestión de asignaciones médicas en MediCore.
 */
public interface IAsignacionMedicoService {

    /**
     * Guarda una nueva asignación médica en la base de datos.
     *
     * @param asignacion entidad de asignación a persistir
     * @return asignación guardada
     */
    AsignacionMedico save(AsignacionMedico asignacion);

    /**
     * Busca la asignación más reciente de un médico ordenada por fecha de fin descendente.
     *
     * @param documentoMedico número de documento del médico
     * @return la asignación más reciente, o vacío si no tiene asignaciones previas
     */
    Optional<AsignacionMedico> findTopByMedicoOrderByFechaFinDesc(String documentoMedico);

    /**
     * Determina el siguiente hospital a asignar en una ciudad usando round-robin.
     *
     * @param codigoCiudad código de la ciudad
     * @return el hospital que le corresponde según la rotación
     */
    Hospital determinarSiguienteHospital(String codigoCiudad);

    /**
     * Determina el siguiente horario a asignar al médico según su historial de rotación.
     *
     * @param documentoMedico número de documento del médico
     * @return el horario que le corresponde en la secuencia
     */
    HorarioMedico determinarSiguienteHorario(String documentoMedico);

    /**
     * Obtiene todas las asignaciones de un médico.
     *
     * @param documentoMedico número de documento del médico
     * @return lista de asignaciones del médico
     */
    List<AsignacionMedico> findByMedico(String documentoMedico);

    /**
     * Obtiene todas las asignaciones de un hospital.
     *
     * @param codigoHospital código del hospital
     * @return lista de asignaciones del hospital
     */
    List<AsignacionMedico> findByHospital(String codigoHospital);

    /**
     * Desactiva una asignación médica por su código.
     *
     * @param codigo identificador de la asignación
     * @return true si fue desactivada, false si no existe
     */
    boolean desactivar(Integer codigo);
}
