package com.medicore.api.dtos.Paciente;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PacienteResponseDTO {
    private String documento;
    private String nombre;
    private String apellido;
    private String eps;
    private String ciudad;
    private String telefono;

}
