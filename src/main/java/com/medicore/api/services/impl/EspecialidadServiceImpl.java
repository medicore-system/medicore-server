package com.medicore.api.services.impl;

import com.medicore.api.entities.Especialidad;
import com.medicore.api.repositories.IEspecialidadRepository;
import com.medicore.api.services.IEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio encargado
 * de gestionar las operaciones relacionadas
 * con las especialidades médicas.
 */
@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements IEspecialidadService {

    /**
     * Repositorio de acceso a datos de especialidades.
     */
    private final IEspecialidadRepository especialidadRepository;

    /**
     * Obtiene la lista de todas las especialidades registradas.
     *
     * @return lista de especialidades
     */
    @Override
    public List<Especialidad> findAll() {
        return especialidadRepository.findAll();
    }

    /**
     * Busca una especialidad por su identificador.
     *
     * @param id identificador de la especialidad
     * @return especialidad encontrada o vacío si no existe
     */
    @Override
    public Optional<Especialidad> findById(Integer id) {
        return especialidadRepository.findById(id);
    }
}