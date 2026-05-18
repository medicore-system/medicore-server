package com.medicore.api.dtos.AsignacionMedico;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AsignacionMedicoRequestDTO {
    private String documentoMedico;
    private String codigoCiudad;
    @Min(value = 1, message = "la duracion minima es de 1 dia")
    private Integer duracionDias;
    private String codigoHospital;
}
