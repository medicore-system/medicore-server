package com.medicore.api.services;

import com.medicore.api.entities.Eps;

import java.util.Optional;

public interface IEpsService {
    Optional<Eps> findById(String codigo);
}
