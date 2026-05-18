package com.medicore.api.dtos.HorarioMedico;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HorarioMedicoRequestDTO {
    private String horario;
    private String siguienteHorario;
}
