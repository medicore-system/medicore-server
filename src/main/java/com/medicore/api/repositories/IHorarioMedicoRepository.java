package com.medicore.api.repositories;

import com.medicore.api.entities.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IHorarioMedicoRepository extends JpaRepository<HorarioMedico, Integer> {
    Optional<HorarioMedico> findByHorario(String horario);
}
