package com.medicore.api.services.impl;

import com.medicore.api.dtos.tarifa.TarifaEpsRequestDTO;
import com.medicore.api.dtos.tarifa.TarifaEpsResponseDTO;
import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Servicio;
import com.medicore.api.entities.costos.TarifaEps;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.mappers.TarifaEpsMapper;
import com.medicore.api.repositories.IEpsRepository;
import com.medicore.api.repositories.ServicioRepository;
import com.medicore.api.repositories.costos.TarifaEpsRepository;
import com.medicore.api.services.ITarifaEpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TarifaEpsServiceImpl implements ITarifaEpsService {

    private final TarifaEpsRepository tarifaEpsRepository;
    private final IEpsRepository epsRepository;
    private final ServicioRepository servicioRepository;
    private final TarifaEpsMapper tarifaEpsMapper;

    @Override
    @Transactional
    public TarifaEpsResponseDTO crearTarifa(TarifaEpsRequestDTO request) {
        // 1. Validar que la EPS exista
        Eps eps = epsRepository.findById(request.codigoEps())
                .orElseThrow(() -> new RecursoNoEncontradoException("EPS no encontrada con el código: " + request.codigoEps()));

        // 2. Validar que el Servicio exista
        Servicio servicio = servicioRepository.findById(request.codigoServicio())
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado con el código: " + request.codigoServicio()));

        // 3. Validar Regla de Negocio: No duplicar tarifas para la misma EPS y Servicio
        if (tarifaEpsRepository.existsByEpsCodigoAndServicioCodigo(eps.getCodigo(), servicio.getCodigo())) {
            // Reutilizamos IllegalArgumentException o podrías crear una TarifaDuplicadaException
            throw new IllegalArgumentException("Ya existe una tarifa configurada para esta EPS y este Servicio.");
        }

        // 4. Construir la entidad
        TarifaEps nuevaTarifa = TarifaEps.builder()
                .eps(eps)
                .servicio(servicio)
                .porcentajeCobertura(request.porcentajeCobertura())
                .estado(true)
                .build();

        // 5. Guardar en base de datos y retornar DTO
        TarifaEps tarifaGuardada = tarifaEpsRepository.save(nuevaTarifa);
        return tarifaEpsMapper.toResponseDTO(tarifaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarifaEpsResponseDTO> obtenerTarifasActivasPorEps(String codigoEps) {
        // Validamos que la EPS exista antes de buscar sus tarifas
        if (!epsRepository.existsById(codigoEps)) {
            throw new RecursoNoEncontradoException("EPS no encontrada con el código: " + codigoEps);
        }

        List<TarifaEps> tarifas = tarifaEpsRepository.findByEpsCodigoAndEstadoTrue(codigoEps);
        
        return tarifas.stream()
                .map(tarifaEpsMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}