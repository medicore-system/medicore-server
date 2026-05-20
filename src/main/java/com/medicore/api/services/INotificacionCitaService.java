package com.medicore.api.services;

import com.medicore.api.entities.Cita.NotificacionCita;

import java.util.List;
import java.util.Optional;

public interface INotificacionCitaService {
    NotificacionCita save(NotificacionCita notificacionCita);
    List<NotificacionCita> findByCorreo(String correo);
    boolean delete(Integer codigo);
}
