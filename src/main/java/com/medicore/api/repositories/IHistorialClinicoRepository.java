package com.medicore.api.repositories;

import com.medicore.api.entities.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistorialClinicoRepository extends JpaRepository<HistorialClinico, String> {
}