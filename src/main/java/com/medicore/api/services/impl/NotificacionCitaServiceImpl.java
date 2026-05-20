package com.medicore.api.services.impl;

import com.medicore.api.entities.Cita.NotificacionCita;
import com.medicore.api.entities.Medico;
import com.medicore.api.repositories.cita.INotificacionCitaRepository;
import com.medicore.api.services.INotificacionCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificacionCitaServiceImpl implements INotificacionCitaService {

    private final INotificacionCitaRepository notificacionCitaRepository;

    @Override
    public Optional<NotificacionCita> findByCorreo(String correo) {
        return notificacionCitaRepository.findByCorreo(correo);
    }

    @Override
    public boolean delete(Integer codigo) {
        Optional<NotificacionCita> notificacionOptional = notificacionCitaRepository.findById(codigo);

        if (notificacionOptional.isPresent()) {

            NotificacionCita notificacionCita = notificacionOptional.get();

            notificacionCita.setEstado(true);

            notificacionCitaRepository.save(notificacionCita);

            return true;
        }

        return false;
    }
}
