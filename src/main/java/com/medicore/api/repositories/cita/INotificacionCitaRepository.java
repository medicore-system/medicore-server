package com.medicore.api.repositories.cita;

import com.medicore.api.entities.Cita.NotificacionCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface INotificacionCitaRepository extends JpaRepository<NotificacionCita, Integer> {
    Optional<NotificacionCita> findByCorreo(String correo);
}
