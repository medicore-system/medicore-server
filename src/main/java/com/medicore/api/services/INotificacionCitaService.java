package com.medicore.api.services;

import com.medicore.api.entities.Cita.NotificacionCita;

import java.util.Optional;

public interface INotificacionCitaService {
    Optional<NotificacionCita> findByCorreo(String correo);
    boolean delete(Integer codigo);
}
