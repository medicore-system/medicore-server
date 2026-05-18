package com.medicore.api.dtos;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String documento;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String telefono;
    private String codigoCiudad;
    private String codigoEPS;
}
