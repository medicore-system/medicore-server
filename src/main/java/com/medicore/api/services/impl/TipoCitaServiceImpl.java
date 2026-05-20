package com.medicore.api.services.impl;

import com.medicore.api.entities.Cita.TipoCita;
import com.medicore.api.repositories.cita.ITipoCitaRepository;
import com.medicore.api.services.ITipoCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoCitaServiceImpl implements ITipoCitaService {
    private final ITipoCitaRepository tipoCitaRepository;

    @Override
    public List<TipoCita> findAll() {
        return tipoCitaRepository.findAll();
    }
}
