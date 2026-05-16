package com.medicore.api.services.impl;

import com.medicore.api.entities.Medico;
import com.medicore.api.repositories.IMedicoRepository;
import com.medicore.api.services.IMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio encargado
 * de gestionar las operaciones relacionadas
 * con los médicos.
 */
@Service
@RequiredArgsConstructor
public class MedicoServiceImpl implements IMedicoService {

    /**
     * Repositorio de acceso a datos de médicos.
     */
    private final IMedicoRepository medicoRepository;

    /**
     * Obtiene la lista de todos los médicos registrados.
     *
     * @return lista de médicos
     */
    @Override
    public List<Medico> findAll() {
        return medicoRepository.findAll();
    }

    /**
     * Busca un médico por su documento.
     *
     * @param documento documento del médico
     * @return médico encontrado o vacío si no existe
     */
    @Override
    public Optional<Medico> findById(String documento) {
        return medicoRepository.findById(documento);
    }

    /**
     * Guarda un nuevo médico en el sistema.
     *
     * @param medico entidad médico a registrar
     * @return médico guardado
     */
    @Override
    public Medico save(Medico medico) {

        medico.setEstado(true);

        return medicoRepository.save(medico);
    }

    /**
     * Actualiza la información de un médico existente.
     *
     * @param documento documento del médico a actualizar
     * @param medico nuevos datos del médico
     * @return médico actualizado o vacío si no existe
     */
    @Override
    public Optional<Medico> update(String documento, Medico medico) {

        return medicoRepository.findById(documento).map(existing -> {

            existing.setNombre(medico.getNombre());
            existing.setApellido(medico.getApellido());
            existing.setTelefono(medico.getTelefono());
            existing.setEmail(medico.getEmail());
            existing.setEspecialidad(medico.getEspecialidad());
            existing.setCiudad(medico.getCiudad());

            return medicoRepository.save(existing);
        });
    }

    /**
     * Cambia el estado de un médico dentro del sistema.
     *
     * <p>
     * Si el médico está activo, pasa a inactivo,
     * y viceversa.
     * </p>
     *
     * @param documento documento del médico
     * @return true si la operación fue exitosa,
     * false si el médico no existe
     */
    @Override
    public boolean delete(String documento) {

        Optional<Medico> medicoOptional = medicoRepository.findById(documento);

        if (medicoOptional.isPresent()) {

            Medico medico = medicoOptional.get();

            medico.setEstado(!medico.getEstado());

            medicoRepository.save(medico);

            return true;
        }

        return false;
    }
}