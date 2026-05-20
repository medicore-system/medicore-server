package com.medicore.api.repositories.cita;

import com.medicore.api.entities.Cita.NotificacionCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para gestionar entidades {@link NotificacionCita} en la base de datos.
 */
@Repository
public interface INotificacionCitaRepository extends JpaRepository<NotificacionCita, Integer> {

    /**
     * Busca todas las notificaciones enviadas a un correo electrónico.
     *
     * @param correo dirección de correo del destinatario
     * @return lista de notificaciones asociadas al correo
     */
    List<NotificacionCita> findByCorreo(String correo);
}
