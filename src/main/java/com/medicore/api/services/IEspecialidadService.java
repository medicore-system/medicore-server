package com.medicore.api.services;

import com.medicore.api.entities.Especialidad;
import com.medicore.api.entities.Medico;

import java.util.List;
import java.util.Optional;

public interface IEspecialidadService {
    List<Especialidad> findAll();
    Optional<Especialidad> findById(Integer id);
}
