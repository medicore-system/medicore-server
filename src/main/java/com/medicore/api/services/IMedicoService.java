package com.medicore.api.services;

import com.medicore.api.dtos.doctor.MedicoRequestDTO;
import com.medicore.api.dtos.doctor.MedicoResponseDTO;
import com.medicore.api.entities.Medico;

import java.util.List;
import java.util.Optional;

public interface IMedicoService {
    List<Medico> findAll();

    Optional<Medico> findById(String documento);

    Medico save(Medico medico);

    Optional<Medico> update(String documento, Medico medico);

    boolean delete(String documento);
}
