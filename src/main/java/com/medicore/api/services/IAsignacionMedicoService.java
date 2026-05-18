package com.medicore.api.services;

import com.medicore.api.entities.AsignacionMedico;
import com.medicore.api.entities.HorarioMedico;
import com.medicore.api.entities.hospital.Hospital;

import java.util.List;
import java.util.Optional;

public interface IAsignacionMedicoService {

    AsignacionMedico save(AsignacionMedico asignacion);

    Optional<AsignacionMedico> findTopByMedicoOrderByFechaFinDesc(String documentoMedico);

    Hospital determinarSiguienteHospital(String codigoCiudad);

    HorarioMedico determinarSiguienteHorario(String documentoMedico);

    List<AsignacionMedico> findByMedico(String documentoMedico);

    List<AsignacionMedico> findByHospital(String codigoHospital);

    boolean desactivar(Integer codigo);
}
