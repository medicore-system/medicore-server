package com.medicore.api.services.impl;

import com.medicore.api.entities.TipoCita;
import com.medicore.api.repositories.ITipoCitaRepository;
import com.medicore.api.services.ITipoCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio encargado de la lógica
 * de negocio relacionada con los Tipos de Citas.
 *
 * <p>Esta clase administra operaciones CRUD
 * relacionadas con un tipo de cita.</p>
 *
 * @author Manuel
 */
@Service
@RequiredArgsConstructor
public class TipoCitaServiceImpl implements ITipoCitaService {

    /**
     * Repositorio encargado de la persistencia
     * de TipoCita.
     */
    private final ITipoCitaRepository tipoCitaRepository;

    /**
     * Obtiene todos los tipos de cita registrados.
     *
     * @return lista de tipos de citas.
     */
    @Override
    public List<TipoCita> findAll() {
        return tipoCitaRepository.findAll();
    }

    /**
     * Busca un TipoCita por su id.
     *
     * @param id id del TipoCita.
     * @return Optional con el TipoCita encontrado
     * o vacío si no existe.
     */
    @Override
    public Optional<TipoCita> findById(Integer id) {
        return tipoCitaRepository.findById(id);
    }
}
