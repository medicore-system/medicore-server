package com.medicore.api.services.impl;

import com.medicore.api.entities.Eps;
import com.medicore.api.repositories.IEpsRepository;
import com.medicore.api.services.IEpsService;

import java.util.Optional;

public class EpsServiceImpl implements IEpsService {

    private IEpsRepository epsRepository;

    @Override
    public Optional<Eps> findById(String codigo) {
        return epsRepository.findById(codigo);
    }
}
