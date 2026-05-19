package com.medicore.api.dtos.reportes;

public record ProductividadMedicoDTO(
        String documentoMedico,
        String nombreMedico,
        String apellidosMedico,
        Long citasCompletadas
) {
}