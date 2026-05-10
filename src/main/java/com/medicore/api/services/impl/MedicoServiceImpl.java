package com.medicore.api.services.impl;

import com.medicore.api.dtos.doctor.MedicoRequestDTO;
import com.medicore.api.dtos.doctor.MedicoResponseDTO;
import com.medicore.api.entities.Medico;
import com.medicore.api.repositories.MedicoRepository;
import com.medicore.api.services.IMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class MedicoServiceImpl implements IMedicoService {
    private final MedicoRepository medicoRepository;
    @Override
    public List<Medico> findAll() {
        return medicoRepository.findAll();
    }

    @Override
    public Optional<Medico> findById(String documento) {
        return medicoRepository.findById(documento);
    }

    @Override
    public Medico save(Medico medico) {
        medico.setEstado(true);
        return medicoRepository.save(medico);
    }

    @Override
    public Optional<Medico> update(String documento, Medico medico) {
        //exiting es el objeto que ya existe en la base de datos y vas a actualizar
        return medicoRepository.findById(documento).map(existing -> {

            existing.setNombre(medico.getNombre());
            existing.setApellido(medico.getApellido());
            existing.setTelefono(medico.getTelefono());
            existing.setCorreo(medico.getCorreo());

            existing.setEspecialidad(medico.getEspecialidad());
            existing.setCiudad(medico.getCiudad());
            existing.setUsuario(medico.getUsuario());

            return medicoRepository.save(existing);
        });
    }

    @Override
    public boolean delete(String documento) {
        if (!medicoRepository.existsById(documento)) return false;
        medicoRepository.deleteById(documento);
        return true;
    }
}
