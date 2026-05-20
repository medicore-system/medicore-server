package com.medicore.api.services;

import com.medicore.api.entities.Cita.NotificacionCita;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio que define las operaciones de gestión de notificaciones de cita en MediCore.
 */
public interface INotificacionCitaService {

    /**
     * Guarda una nueva notificación de cita en el sistema.
     *
     * @param notificacionCita entidad de notificación a persistir
     * @return notificación guardada
     */
    NotificacionCita save(NotificacionCita notificacionCita);

    /**
     * Busca todas las notificaciones asociadas a un correo electrónico.
     *
     * @param correo dirección de correo del destinatario
     * @return lista de notificaciones del correo
     */
    List<NotificacionCita> findByCorreo(String correo);

    /**
     * Elimina una notificación por su código.
     *
     * @param codigo identificador de la notificación
     * @return true si fue eliminada, false si no existe
     */
    boolean delete(Integer codigo);
}
