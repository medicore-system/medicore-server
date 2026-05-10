package com.medicore.api.dtos.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioCreateRequestDTO {
    private String documento;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String codigo_eps;
    private String codigo_ciudad;
}
