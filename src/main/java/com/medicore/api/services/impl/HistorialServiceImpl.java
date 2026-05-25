package com.medicore.api.services.impl;

import com.medicore.api.dtos.historial.HistorialRequest;
import com.medicore.api.dtos.historial.HistorialResponse;
import com.medicore.api.entities.HistorialClinico;
import com.medicore.api.entities.Medico;
import com.medicore.api.entities.Usuario;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.repositories.IHistorialClinicoRepository;
import com.medicore.api.repositories.IMedicoRepository;
import com.medicore.api.repositories.IUsuarioRepository;
import com.medicore.api.services.IHistorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de la lógica de negocio para la gestión de historiales clínicos.
 */
@Service
@RequiredArgsConstructor
public class HistorialServiceImpl implements IHistorialService {

    private final IHistorialClinicoRepository historialRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IMedicoRepository medicoRepository;

    /**
     * Crea y persiste un nuevo historial clínico.
     *
     * @param request datos del historial a registrar.
     * @return historial creado como DTO de respuesta.
     * @throws RecursoNoEncontradoException si el paciente o médico no existen.
     */
    @Override
    @Transactional
    public HistorialResponse crearHistorial(HistorialRequest request) {
        Usuario paciente = usuarioRepository.findById(request.getDocumentoPaciente())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Paciente no encontrado con documento: " + request.getDocumentoPaciente()));

        Medico medico = medicoRepository.findById(request.getDocumentoMedico())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Médico no encontrado con documento: " + request.getDocumentoMedico()));

        if (historialRepository.existsById(request.getCodigo())) {
            throw new IllegalArgumentException(
                    "Ya existe un historial clínico con el código: " + request.getCodigo());
        }

        HistorialClinico historial = HistorialClinico.builder()
                .codigo(request.getCodigo())
                .fecha(request.getFecha())
                .tipo(request.getTipo())
                .descripcion(request.getDescripcion())
                .paciente(paciente)
                .medico(medico)
                .build();

        historialRepository.save(historial);
        return toResponse(historial, paciente, medico);
    }

    /**
     * Lista todos los historiales clínicos registrados.
     *
     * @return lista de historiales como DTOs de respuesta.
     */
    @Override
    @Transactional(readOnly = true)
    public List<HistorialResponse> listarHistoriales() {
        return historialRepository.findAll()
                .stream()
                .map(h -> toResponse(h, h.getPaciente(), h.getMedico()))
                .toList();
    }

    /**
     * Convierte una entidad HistorialClinico a su DTO de respuesta.
     */
    private HistorialResponse toResponse(HistorialClinico historial, Usuario paciente, Medico medico) {
        return HistorialResponse.builder()
                .codigo(historial.getCodigo())
                .fecha(historial.getFecha())
                .tipo(historial.getTipo())
                .descripcion(historial.getDescripcion())
                .documentoPaciente(paciente.getDocumento())
                .nombrePaciente(paciente.getNombre() + " " + paciente.getApellido())
                .documentoMedico(medico.getDocumento())
                .nombreMedico(medico.getNombre() + " " + medico.getApellido())
                .build();
    }
}
