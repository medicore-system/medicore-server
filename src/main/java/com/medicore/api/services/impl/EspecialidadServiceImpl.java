package com.medicore.api.services.impl;

import com.medicore.api.entities.Especialidad;
import com.medicore.api.repositories.EspecialidadRepository;
import com.medicore.api.services.IEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements IEspecialidadService {
    private final EspecialidadRepository especialidadRepository;

    @Override
    public List<Especialidad> findAll() {
        return especialidadRepository.findAll();
    }

    @Override
    public Optional<Especialidad> findById(Integer id) {
        return especialidadRepository.findById(id);
    }
}
