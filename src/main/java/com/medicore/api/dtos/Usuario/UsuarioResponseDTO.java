package com.medicore.api.dtos.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioResponseDTO {
    private String documento;
    private String nombre;
    private String apellido;
    private String correo;
    private String eps;
    private String ciudad;
    private String telefono;
    private String rol;
    private boolean estado;

}
