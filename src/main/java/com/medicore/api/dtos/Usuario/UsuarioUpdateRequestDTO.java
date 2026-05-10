package com.medicore.api.dtos.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioUpdateRequestDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String codigo_eps;
    private String codigo_ciudad;
}
