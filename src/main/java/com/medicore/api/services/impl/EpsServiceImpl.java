package com.medicore.api.services.impl;

import com.medicore.api.entities.Eps;
import com.medicore.api.repositories.IEpsRepository;
import com.medicore.api.services.IEpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio encargado de la lógica
 * de negocio relacionada con las eps.
 *
 * <p>Esta clase administra operaciones CRUD
 * relacionadas con una eps.</p>
 *
 * @author Manuel
 */
@Service
@RequiredArgsConstructor
public class EpsServiceImpl implements IEpsService {

    /**
     * Repositorio encargado de la persistencia
     * de Eps.
     */
    private final IEpsRepository epsRepository;

    /**
     * Obtiene todas las Eps registradas.
     *
     * @return lista de todas las Eps.
     */
    @Override
    public List<Eps> findAll() {
        return epsRepository.findAll();
    }

    /**
     * Busca una Eps por su codigo.
     *
     * @param codigo codigo de la Eps.
     * @return Optional con la Eps encontrada
     * o vacío si no existe.
     */
    @Override
    public Optional<Eps> findById(String codigo) {
        return epsRepository.findById(codigo);
    }


}
