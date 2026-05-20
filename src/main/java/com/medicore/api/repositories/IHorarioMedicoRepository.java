package com.medicore.api.repositories;

import com.medicore.api.entities.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para gestionar entidades {@link HorarioMedico} en la base de datos.
 */
@Repository
public interface IHorarioMedicoRepository extends JpaRepository<HorarioMedico, Integer> {

    /**
     * Busca un horario médico por su descripción exacta.
     *
     * @param horario descripción del horario (ej: "MAÑANA", "TARDE")
     * @return el horario encontrado, o vacío si no existe
     */
    Optional<HorarioMedico> findByHorario(String horario);
}
