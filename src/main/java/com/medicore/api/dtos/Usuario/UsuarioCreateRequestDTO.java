package com.medicore.api.dtos.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO utilizado para recibir la información necesaria
 * para registrar un nuevo usuario en el sistema.
 *
 * <p>Contiene los datos enviados desde la petición HTTP
 * para la creación de usuarios.</p>
 *
 * @author Manuel
 */
@Data
@NoArgsConstructor
public class UsuarioCreateRequestDTO {

    /**
     * Documento de identificación del usuario.
     */
    private String documento;

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Apellido del usuario.
     */
    private String apellido;

    /**
     * Correo electrónico del usuario.
     */
    private String correo;

    /**
     * Número telefónico del usuario.
     */
    private String telefono;

    /**
     * Código de la EPS asociada al usuario.
     */
    private String codigo_eps;

    /**
     * Código de la ciudad asociada al usuario.
     */
    private String codigo_ciudad;
}
