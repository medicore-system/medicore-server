package com.medicore.api.services;

import com.medicore.api.entities.Especialidad;
import com.medicore.api.entities.Medico;

import java.util.Optional;

public interface IEspecialidadService {
    Optional<Especialidad> findById(Integer id);
}
