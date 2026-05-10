package com.medicore.api.repositories;

import com.medicore.api.entities.Eps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEpsRepository extends JpaRepository<Eps, String> {
    Optional<Eps> findByNombre(String nombre);
}
