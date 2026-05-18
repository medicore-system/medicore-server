package com.medicore.api.tasks;

import com.medicore.api.repositories.IAsignacionMedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AsignacionMedicoTask {

    private final IAsignacionMedicoRepository asignacionRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void desactivarAsignacionesVencidas() {
        asignacionRepository.findByEstadoTrueAndFechaFinBefore(LocalDate.now())
                .forEach(asignacion -> {
                    asignacion.setEstado(false);
                    asignacionRepository.save(asignacion);
                });
    }
}