package com.medicore.api.dtos.notificacionCita;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificacionCitaResponseDTO {
    private Integer codigo;
    private Boolean estado;
    private String descripcion;
    private String correoDestino;
    private String codigoCita;
}
