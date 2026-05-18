package com.medicore.api.services.impl;

import com.medicore.api.entities.AsignacionMedico;
import com.medicore.api.entities.HorarioMedico;
import com.medicore.api.entities.hospital.Hospital;
import com.medicore.api.repositories.IAsignacionMedicoRepository;
import com.medicore.api.repositories.IHorarioMedicoRepository;
import com.medicore.api.repositories.hospital.HospitalRepository;
import com.medicore.api.services.IAsignacionMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AsignacionMedicoServiceImpl implements IAsignacionMedicoService {

    private final IAsignacionMedicoRepository asignacionMedicoRepository;
    private final IHorarioMedicoRepository horarioMedicoRepository;
    private final HospitalRepository hospitalRepository;
    private static final String HORARIO_INICIAL = "8:00am - 4:00pm";

    @Override
    public AsignacionMedico save(AsignacionMedico asignacion) {
        return asignacionMedicoRepository.save(asignacion);
    }

    @Override
    public Optional<AsignacionMedico> findTopByMedicoOrderByFechaFinDesc(String documentoMedico) {
        return asignacionMedicoRepository.findTopByMedicoDocumentoOrderByFechaFinDesc(documentoMedico);
    }

    @Override
    public Hospital determinarSiguienteHospital(String codigoCiudad) {
        List<Hospital> hospitales = hospitalRepository.findByCiudadCodigoAndEstadoTrue(codigoCiudad);
        if(hospitales.isEmpty()){
            throw new RuntimeException("No hay hospitales activos en la ciudad: " +  codigoCiudad);
        }
        Optional<AsignacionMedico> ultima = asignacionMedicoRepository.findTopByCiudadCodigoOrderByCodigoDesc(codigoCiudad);

        if(ultima.isEmpty()){
            return hospitales.get(0);
        }

        String ultimoHospitalCodigo = ultima.get().getCiudad().getCodigo();

        int indexActual = IntStream.range(0, hospitales.size())
                .filter(i -> hospitales.get(i).getCodigo().equals(ultimoHospitalCodigo))
                .findFirst()
                .orElse(-1);

        return hospitales.get((indexActual + 1) % hospitales.size());
    }

    @Override
    public HorarioMedico determinarSiguienteHorario(String documentoMedico) {
        Optional<AsignacionMedico> ultimoTurno = asignacionMedicoRepository
                .findTopByMedicoDocumentoOrderByFechaFinDesc(documentoMedico);

        String siguienteHorario;

        if (ultimoTurno.isEmpty()) {
            siguienteHorario = HORARIO_INICIAL;
        } else {
            siguienteHorario = ultimoTurno.get().getHorario().getSiguienteHorario();
            if (siguienteHorario == null) {
                throw new RuntimeException("El horario no tiene siguiente configurado");
            }
        }

        return horarioMedicoRepository.findByHorario(siguienteHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado: " + siguienteHorario));
    }

    @Override
    public List<AsignacionMedico> findByMedico(String documentoMedico) {
        return asignacionMedicoRepository.findByMedicoDocumentoAndEstadoTrue(documentoMedico);
    }

    @Override
    public List<AsignacionMedico> findByHospital(String codigoHospital) {
        return asignacionMedicoRepository.findByHospitalCodigoAndEstadoTrue(codigoHospital);
    }

    @Override
    public boolean desactivar(Integer codigo) {
        Optional<AsignacionMedico> optional = asignacionMedicoRepository.findById(codigo);
        if (optional.isPresent()) {
            optional.get().setEstado(false);
            asignacionMedicoRepository.save(optional.get());
            return true;
        }
        return false;
    }
}
